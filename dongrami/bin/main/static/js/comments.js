// assets/js/comments.js

let currentPage = 1;
const itemsPerPage = 10;
const comments = [{ text: "홍길동의 첫 번째 댓글 내용이 여기에 표시됩니다.", date: "2023-06-01" }]; // 예시 댓글 하나
let activeDropdown = null;
let editIndex = null;

function changeImage() {
    // 이미지 변경 기능 구현
}

function updateSelectedCount() {
    const selectedCount = document.querySelectorAll('.select-post:checked').length;
    const totalCount = Math.min(comments.length - (currentPage - 1) * itemsPerPage, itemsPerPage);
    document.getElementById('selected-count').textContent = `${selectedCount}/${totalCount} 선택됨`;
}

function updateCommentCount() {
    const totalCount = comments.length;
    document.getElementById('tarot-count').textContent = `내가 쓴 글: ${totalCount}`;
}

function toggleSelectAll() {
    const selectAllCheckbox = document.getElementById('select-all-checkbox');
    const postCheckboxes = document.querySelectorAll('.select-post');
    postCheckboxes.forEach(checkbox => {
        checkbox.checked = selectAllCheckbox.checked;
    });
    updateSelectedCount();
}

function toggleDeleteAllButton() {
    updateSelectedCount();
}

function deleteSelectedPosts() {
    if (confirm("한번 삭제한 자료는 복구할 방법이 없습니다.\n\n정말 삭제하시겠습니까?")) {
        const postCheckboxes = document.querySelectorAll('.select-post:checked');
        postCheckboxes.forEach(checkbox => {
            const index = Array.from(checkbox.closest('tbody').children).indexOf(checkbox.closest('tr')) + (currentPage - 1) * itemsPerPage;
            comments.splice(index, 1);
        });
        renderTable(currentPage);
        updateSelectedCount();
        updateCommentCount();
    }
}

function editComment(index) {
    editIndex = index;
    const comment = comments[index];
    document.getElementById('edit-topic').value = `주제 ${index + 1}`;
    document.getElementById('edit-comment').value = comment.text;
    document.getElementById('edit-modal').style.display = 'block';
}

function closeModal() {
    document.getElementById('edit-modal').style.display = 'none';
    editIndex = null;
}

function saveEdit(event) {
    event.preventDefault();
    const editedComment = document.getElementById('edit-comment').value;

    if (editIndex !== null) {
        comments[editIndex].text = editedComment;
        renderTable(currentPage);
        closeModal();
        alert('해당 내용이 수정되었습니다.');
    }
}

function confirmDelete(index) {
    if (confirm("한번 삭제한 자료는 복구할 방법이 없습니다.\n\n정말 삭제하시겠습니까?")) {
        comments.splice(index, 1);
        renderTable(currentPage);
        updateSelectedCount();
        updateCommentCount();
    }
}

function confirmDeleteInModal() {
    if (confirm("한번 삭제한 자료는 복구할 방법이 없습니다.\n\n해당 댓글을 삭제하시겠습니까?")) {
        comments.splice(editIndex, 1);
        renderTable(currentPage);
        closeModal();
        updateSelectedCount();
        updateCommentCount();
    }
}

function addComment(commentText) {
    if (commentText.trim() === '') {
        alert('댓글을 입력하세요.');
        return;
    }

    const currentDate = new Date().toISOString().split('T')[0]; // 현재 날짜 (YYYY-MM-DD 형식)
    comments.push({ text: commentText, date: currentDate });
    renderTable(currentPage);
    updateSelectedCount();
    updateCommentCount();
}

function renderTable(page) {
    currentPage = page;
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const tableBody = document.getElementById('comment-table-body');
    tableBody.innerHTML = '';

    comments.slice(start, end).forEach((comment, index) => {
        const row = document.createElement('tr');
        const globalIndex = index + start;
        row.innerHTML = `
            <td><input type="checkbox" class="select-post" onclick="toggleDeleteAllButton()"></td>
            <td>주제 ${globalIndex + 1}</td>
            <td>${comment.text}</td>
            <td>
                <span>${comment.date}</span>
                <div class="dropdown">
                    <button class="dropdown-button" onclick="toggleDropdown(this)">⋮</button>
                    <div class="dropdown-content">
                        <button class="edit-button" onclick="editComment(${globalIndex})">수정</button>
                        <button class="delete-button" onclick="confirmDelete(${globalIndex})">삭제</button>
                    </div>
                </div>
            </td>
        `;
        tableBody.appendChild(row);
    });

    renderPagination();
    updateSelectedCount();
    document.getElementById('select-all-checkbox').checked = false; // 페이지를 넘길 때 전체 선택 체크박스를 해제
}

function renderPagination() {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';
    const totalPages = Math.ceil(comments.length / itemsPerPage);

    for (let i = 1; i <= totalPages; i++) {
        const button = document.createElement('button');
        button.textContent = i;
        button.className = i === currentPage ? 'active' : '';
        button.onclick = () => renderTable(i);
        pagination.appendChild(button);
    }
}

function toggleDropdown(button) {
    const dropdownContent = button.nextElementSibling;

    if (activeDropdown && activeDropdown !== dropdownContent) {
        activeDropdown.classList.remove('show');
    }

    dropdownContent.classList.toggle('show');
    activeDropdown = dropdownContent.classList.contains('show') ? dropdownContent : null;
}

// 페이지 다른 곳을 클릭하면 드롭다운이 닫히도록 설정
document.addEventListener('click', function(event) {
    if (activeDropdown && !event.target.closest('.dropdown')) {
        activeDropdown.classList.remove('show');
        activeDropdown = null;
    }
});

// 댓글 추가 예시 (다른 페이지에서 댓글을 작성했을 때 호출되는 함수)
function newCommentFromAnotherPage(commentText) {
    addComment(commentText);
}

// 초기 테이블 렌더링
document.addEventListener('DOMContentLoaded', () => {
    renderTable(currentPage);
});
