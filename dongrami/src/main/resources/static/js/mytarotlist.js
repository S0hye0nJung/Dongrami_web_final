document.addEventListener('DOMContentLoaded', () => {
    let comments = [];
    const commentsPerPage = 10;
    let currentPage = 1;

    async function initialize() {
        try {
            await fetchComments(); // 초기 댓글 로드
            setupEventListeners(); // 이벤트 리스너 설정
            render(); // 댓글과 페이지네이션 렌더링
        } catch (error) {
            console.error('초기화 오류:', error);
        }
    }

    function render() {
        displayComments(currentPage);
    }

    function displayComments(page) {
        const commentSection = document.getElementById('comment-section');
        if (!commentSection) {
            console.error('"comment-section" id를 가진 요소를 찾을 수 없습니다.');
            return;
        }
        commentSection.innerHTML = '';

        const table = document.createElement('table');
        table.className = 'comment-table';

        const header = document.createElement('tr');
        header.innerHTML = `
            <th><input type="checkbox" id="select-all"></th>
            <th>번호</th>
            <th>대주제</th>
            <th>소주제</th>
            <th>타로 본 날짜</th>
            <th>관리</th>
        `;
        table.appendChild(header);

        const startIndex = (page - 1) * commentsPerPage;
        const endIndex = Math.min(startIndex + commentsPerPage, comments.length);

        if (comments.length === 0) {
            const noCommentsRow = document.createElement('tr');
            const noCommentsCell = document.createElement('td');
            noCommentsCell.colSpan = 6; // 모든 열을 span
            noCommentsCell.id = 'no-comments';
            noCommentsCell.textContent = '작성된 댓글이 없습니다.';
            noCommentsRow.appendChild(noCommentsCell);
            table.appendChild(noCommentsRow);
        } else {
            for (let i = startIndex; i < endIndex; i++) {
                const comment = comments[i];
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><input type="checkbox" class="select-comment" data-comment-id="${comment.resultId}"></td>
                    <td>${comments.length - i}</td>
                    <td>${comment.maincategoryName}</td>
                    <td>${comment.subcategoryName}</td>
                    <td>${formatDate(comment.resultDate)}</td>
                    <td><button class="edit-button" data-comment-id="${comment.resultId}">보기</button></td>
                `;
                table.appendChild(row);
            }
        }

        commentSection.appendChild(table);

        renderPagination(comments.length, commentsPerPage, page);

        // "선택 삭제" 버튼에 이벤트 리스너 추가
        const deleteSelectedButton = document.getElementById('delete-selected');
        if (deleteSelectedButton) {
            deleteSelectedButton.addEventListener('click', async () => {
                await deleteSelectedComments();
            });
        }

        // 페이지네이션 버튼에 이벤트 리스너 추가
        const paginationButtons = document.querySelectorAll('.page-button');
        paginationButtons.forEach(button => {
            button.addEventListener('click', () => {
                currentPage = parseInt(button.textContent, 10);
                displayComments(currentPage);
            });
        });

        // "전체 선택" 체크박스에 이벤트 리스너 추가
        const selectAllCheckbox = document.getElementById('select-all');
        if (selectAllCheckbox) {
            selectAllCheckbox.addEventListener('change', function() {
                const checkboxes = document.querySelectorAll('.select-comment');
                checkboxes.forEach(checkbox => {
                    checkbox.checked = this.checked;
                });
                updateSelectedCount();
                toggleSelectedCountDisplay();
            });
        }

        // 개별 체크박스에 이벤트 리스너 추가
        const checkboxes = document.querySelectorAll('.select-comment');
        checkboxes.forEach(checkbox => {
            checkbox.addEventListener('change', () => {
                const allChecked = Array.from(checkboxes).every(chk => chk.checked);
                document.getElementById('select-all').checked = allChecked;
                updateSelectedCount();
                toggleSelectedCountDisplay();
            });
        });
    }

async function deleteSelectedComments() {
    const selectedComments = document.querySelectorAll('.select-comment:checked');
    const commentIds = Array.from(selectedComments).map(checkbox => parseInt(checkbox.dataset.commentId));

    if (commentIds.length === 0) {
        alert('삭제할 댓글을 선택해주세요.');
        return;
    }

    try {
        const response = await fetch('/delete-comment', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ ids: commentIds })
        });

        if (!response.ok) {
            const errorMessage = await response.text(); // 서버에서 보낸 오류 메시지 읽기
            throw new Error(`댓글 삭제에 실패했습니다: ${errorMessage}`);
        }

        // 삭제 성공 시, 현재 페이지 댓글 목록 갱신
        await fetchComments();
        displayComments(currentPage);
        updateSelectedCount();
    } catch (error) {
        console.error('댓글 삭제 오류:', error);
        alert(`댓글 삭제 중 오류가 발생했습니다: ${error.message}`);
    }
}




    function renderPagination(totalComments, commentsPerPage, currentPage) {
        const paginationContainer = document.getElementById('pagination');
        if (!paginationContainer) {
            console.error('"pagination" id를 가진 요소를 찾을 수 없습니다.');
            return;
        }
        paginationContainer.innerHTML = '';

        const totalPages = Math.ceil(totalComments / commentsPerPage);
        for (let i = 1; i <= totalPages; i++) {
            const button = document.createElement('button');
            button.textContent = i;
            button.className = 'page-button';
            if (i === currentPage) {
                button.classList.add('active');
            }
            paginationContainer.appendChild(button);
        }
    }

    function updateSelectedCount() {
        const selectedCount = document.querySelectorAll('.select-comment:checked').length;
        const selectedCountElement = document.getElementById('selected-count');
        if (selectedCountElement) {
            selectedCountElement.textContent = `${selectedCount}/${commentsPerPage} 선택`; // 페이지당 고정된 개수
        }
    }

    function toggleSelectedCountDisplay() {
        const selectedCount = document.querySelectorAll('.select-comment:checked').length;
        const selectedCountElement = document.getElementById('selected-count');
        if (selectedCountElement) {
            selectedCountElement.textContent = `${selectedCount}/${commentsPerPage} 선택`; // 선택된 개수 표시
            selectedCountElement.style.display = selectedCount > 0 ? 'inline' : 'none'; // 선택된 개수에 따라 표시 여부 변경
        }
    }

    function setupEventListeners() {
        // "편집" 버튼 클릭에 이벤트 리스너 추가
        const commentSection = document.getElementById('comment-section');
        if (commentSection) {
            commentSection.addEventListener('click', async (event) => {
                if (event.target.classList.contains('edit-button')) {
                    const commentId = event.target.dataset.commentId;
                    console.log(`ID ${commentId}의 댓글 편집`);
                    // 여기에 특정 댓글 편집을 처리하는 로직 추가 (예: 모달 열기 또는 편집 페이지로 이동)
                }
            });
        }

        // "삭제 선택" 버튼 클릭에 이벤트 리스너 추가
        const deleteSelectedButton = document.getElementById('delete-selected');
        if (deleteSelectedButton) {
            deleteSelectedButton.addEventListener('click', async () => {
                await deleteSelectedComments();
            });
        }
    }

    async function fetchComments() {
        try {
            const response = await fetch('/my-tarot-list');
            if (!response.ok) {
                throw new Error('서버에서 데이터를 가져오는 데 실패했습니다.');
            }
            comments = await response.json(); // 가져온 데이터로 댓글 업데이트
            displayComments(currentPage); // 가져온 댓글 표시
        } catch (error) {
            console.error('댓글 가져오기 오류:', error);
        }
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        let month = (1 + date.getMonth()).toString().padStart(2, '0');
        let day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    initialize(); // 페이지 초기화
});
