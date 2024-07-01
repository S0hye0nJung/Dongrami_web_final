document.addEventListener('DOMContentLoaded', () => {
    const repliesPerPage = 10;
    let currentPage = 1;

    function displayReplies(page) {
        const replySection = document.getElementById('reply-section');
        replySection.innerHTML = '';

        const table = document.createElement('table');
        table.className = 'reply-table';

        const header = document.createElement('tr');
        header.innerHTML = `
            <th><input type="checkbox" id="select-all"></th>
            <th>번호</th>
            <th>투표주제</th>
            <th>댓글내용</th>
            <th>작성날짜</th>
            <th>관리</th>
        `;
        table.appendChild(header);

        const startIndex = (page - 1) * repliesPerPage;
        const endIndex = Math.min(startIndex + repliesPerPage, replies.length);

        if (replies.length === 0) {
            const noRepliesRow = document.createElement('tr');
            const noRepliesCell = document.createElement('td');
            noRepliesCell.colSpan = 6;
            noRepliesCell.id = 'no-replies';
            noRepliesCell.textContent = '작성글이 없습니다.';
            noRepliesRow.appendChild(noRepliesCell);
            table.appendChild(noRepliesRow);
        } else {
            for (let i = startIndex; i < endIndex; i++) {
                const reply = replies[i];
                const row = document.createElement('tr');
                row.dataset.index = i;
                row.dataset.replyId = reply.replyId;
                row.innerHTML = `
                    <td><input type="checkbox" class="select-reply"></td>
                    <td>${replies.length - i}</td>
                    <td>${reply.question || ''}</td>
                    <td><a href="/vote?page=${reply.page}&replyId=${reply.replyId}" data-id="${reply.replyId}" data-page="${reply.page}">${reply.content || ''}</a></td>
                    <td>${reply.replyCreate || ''}</td>
                    <td><button class="edit-button" data-id="${i}" data-topic="${reply.question}" data-content="${reply.content}">수정</button></td>
                `;
                table.appendChild(row);
            }
        }

        replySection.appendChild(table);
        renderPagination(replies.length, repliesPerPage, page);

        document.getElementById('select-all').addEventListener('change', function() {
            const checkboxes = document.querySelectorAll('.select-reply');
            for (let checkbox of checkboxes) {
                checkbox.checked = this.checked;
            }
            updateSelectedCount();
            toggleSelectedCountDisplay();
        });

        const checkboxes = document.querySelectorAll('.select-reply');
        for (let checkbox of checkboxes) {
            checkbox.addEventListener('change', function() {
                if (!this.checked) {
                    document.getElementById('select-all').checked = false;
                } else {
                    const allChecked = Array.from(checkboxes).every(chk => chk.checked);
                    if (allChecked) {
                        document.getElementById('select-all').checked = true;
                    }
                }
                updateSelectedCount();
                toggleSelectedCountDisplay();
            });
        }

        const replyLinks = document.querySelectorAll('.reply-link');
        replyLinks.forEach(link => {
            link.addEventListener('click', function(event) {
                event.preventDefault();
                const replyId = this.getAttribute('data-id');
                const replyPage = this.getAttribute('data-page');
                window.location.href = `/vote?page=${replyPage}&replyId=${replyId}`;
            });
        });

        const editButtons = document.querySelectorAll('.edit-button');
        for (let editButton of editButtons) {
            editButton.addEventListener('click', function() {
                const replyId = this.getAttribute('data-id');
                const replyTopic = this.getAttribute('data-topic');
                const replyContent = this.getAttribute('data-content');
                $('#reply-text').val(replyContent);
                $('#replyId').val(replyId);
                $('#modal-topic').text(`투표 주제: ${replyTopic}`);
                $('#reply-modal').css('display', 'block');
            });
        }

        function updateSelectedCount() {
            const selectedCount = document.querySelectorAll('.select-reply:checked').length;
            document.getElementById('selected-count').textContent = `${selectedCount}/10 선택`;
        }

        function toggleSelectedCountDisplay() {
            const selectedCount = document.querySelectorAll('.select-reply:checked').length;
            const selectedCountElement = document.getElementById('selected-count');
            if (selectedCount > 0) {
                selectedCountElement.style.display = 'inline';
            } else {
                selectedCountElement.style.display = 'none';
            }
        }

        const modal = document.getElementById('deleteModal');
        const confirmDeleteButton = document.getElementById('confirm-delete');
        const cancelDeleteButton = document.getElementById('cancel-delete');

        document.getElementById('delete-selected').addEventListener('click', function() {
            const selectedCount = document.querySelectorAll('.select-reply:checked').length;
            if (selectedCount > 0) {
                modal.style.display = 'block';
            }
        });

        cancelDeleteButton.onclick = function() {
            modal.style.display = 'none';
        }

        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }

        confirmDeleteButton.addEventListener('click', function() {
            const selectedReplies = document.querySelectorAll('.select-reply:checked');
            selectedReplies.forEach(checkbox => {
                const row = checkbox.closest('tr');
                const index = Array.from(row.parentNode.children).indexOf(row) - 1 + startIndex;
                $.ajax({
                    url: `/api/reply/delete/${replies[index].replyId}`,
                    method: 'DELETE',
                    success: () => {
                        replies.splice(index, 1);
                        displayReplies(currentPage);
                    },
                    error: (err) => {
                        console.error('Failed to delete reply', err);
                    }
                });
            });
            modal.style.display = 'none';
        });

        updateSelectedCount();
        toggleSelectedCountDisplay();
    }

    function renderPagination(totalReplies, repliesPerPage, currentPage) {
        const paginationContainer = document.getElementById('pagination');
        paginationContainer.innerHTML = '';
        const pageCount = Math.ceil(totalReplies / repliesPerPage);

        for (let i = 1; i <= pageCount; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.className = 'page-button';
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pageButton.addEventListener('click', () => {
                displayReplies(i);
            });
            paginationContainer.appendChild(pageButton);
        }
    }

    // 댓글 데이터를 서버로부터 로드
    function loadReplies() {
        const userId = '123'; // 실제 사용자 ID로 변경 필요
        $.ajax({
            url: `/myreply?userId=${userId}`, // 사용자 댓글 불러오기
            method: 'GET',
            success: (data) => {
                replies = data;
                displayReplies(currentPage);
            },
            error: (err) => {
                console.error('Failed to load replies', err);
            }
        });
    }

    loadReplies();

    document.getElementById('delete-selected').style.display = 'inline-flex';

    const closeModal = () => {
        $('#reply-modal').css('display', 'none');
    };

    const closeButtons = document.querySelectorAll('.close');
    closeButtons.forEach(button => {
        button.addEventListener('click', closeModal);
    });

    document.getElementById('cancel-reply').addEventListener('click', closeModal);

    document.getElementById('save-reply').addEventListener('click', (event) => {
        event.preventDefault();
        const replyId = $('#replyId').val();
        const updatedContent = $('#reply-text').val();
        $.ajax({
            url: '/api/reply/update',
            method: 'POST',
            data: JSON.stringify({ replyId: replyId, content: updatedContent }),
            contentType: 'application/json',
            success: () => {
                loadReplies();
                closeModal();
            },
            error: (err) => {
                console.error('Failed to update reply', err);
            }
        });
    });

    window.addEventListener('click', (event) => {
        const modal = document.getElementById('reply-modal');
        if (event.target == modal) {
            closeModal();
        }
    });
});
