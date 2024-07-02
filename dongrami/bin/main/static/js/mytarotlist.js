document.addEventListener('DOMContentLoaded', () => {
    let comments = [];
    const commentsPerPage = 10;
    let currentPage = 1;

    async function initialize() {
        try {
            await fetchComments(); // Initial comment load
            setupEventListeners(); // Event listeners setup
            render(); // Render comments and pagination
        } catch (error) {
            console.error('Initialization error:', error);
        }
    }

    function render() {
        displayComments(currentPage);
    }

    function displayComments(page) {
        const commentSection = document.getElementById('comment-section');
        if (!commentSection) {
            console.error('Cannot find element with id "comment-section"');
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
            noCommentsCell.colSpan = 6; // span all columns
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

        // Add event listener for "delete selected" button
        const deleteSelectedButton = document.getElementById('delete-selected');
        if (deleteSelectedButton) {
            deleteSelectedButton.addEventListener('click', async () => {
                await deleteSelectedComments();
            });
        }

        // Add event listeners for pagination buttons
        const paginationButtons = document.querySelectorAll('.page-button');
        paginationButtons.forEach(button => {
            button.addEventListener('click', () => {
                currentPage = parseInt(button.textContent, 10);
                displayComments(currentPage);
            });
        });

        // Add event listener for "select all" checkbox
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

        // Add event listeners for individual checkboxes
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
        const commentIds = Array.from(selectedComments).map(checkbox => checkbox.dataset.commentId);

        try {
            const response = await fetch('/delete-comments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ commentIds })
            });

            if (!response.ok) {
                throw new Error('댓글 삭제에 실패했습니다.');
            }

            const data = await response.json();
            comments = data; // Update comments with new list
            displayComments(currentPage); // Render updated comments
        } catch (error) {
            console.error('Error deleting comments:', error);
            alert('댓글 삭제에 실패했습니다. 다시 시도해 주세요.');
        }
    }

    function renderPagination(totalComments, commentsPerPage, currentPage) {
        const paginationContainer = document.getElementById('pagination');
        if (!paginationContainer) {
            console.error('Cannot find element with id "pagination"');
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
            selectedCountElement.textContent = `${selectedCount}/${commentsPerPage} 선택`; // Fixed per page count
        }
    }

    function toggleSelectedCountDisplay() {
        const selectedCount = document.querySelectorAll('.select-comment:checked').length;
        const selectedCountElement = document.getElementById('selected-count');
        if (selectedCountElement) {
            selectedCountElement.textContent = `${selectedCount}/${commentsPerPage} 선택`; // Display selected count
            selectedCountElement.style.display = selectedCount > 0 ? 'inline' : 'none'; // Toggle display based on selected count
        }
    }

    function setupEventListeners() {
        // Add event listener for "edit" button click
        const commentSection = document.getElementById('comment-section');
        if (commentSection) {
            commentSection.addEventListener('click', async (event) => {
                if (event.target.classList.contains('edit-button')) {
                    const commentId = event.target.dataset.commentId;
                    console.log(`Edit comment with ID ${commentId}`);
                    // Add logic here to handle editing a specific comment (e.g., open a modal or navigate to an edit page)
                }
            });
        }

        // Add event listener for trash icon click
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
            comments = await response.json(); // Update comments with fetched data
            displayComments(currentPage); // Display fetched comments
        } catch (error) {
            console.error('Error fetching comments:', error);
        }
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        let month = (1 + date.getMonth()).toString().padStart(2, '0');
        let day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    initialize(); // Initialize page
});