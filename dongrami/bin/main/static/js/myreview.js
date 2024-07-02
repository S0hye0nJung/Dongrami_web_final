document.addEventListener('DOMContentLoaded', () => {
    let comments = [];
    const commentsPerPage = 10;
    let currentPage = 1;

    function fetchComments() {
        fetch('/reviews')
            .then(response => {
                if (!response.ok) {
                    throw new Error('리뷰를 가져오는 중 오류가 발생했습니다: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                comments = data;
                displayComments(currentPage);
                fetchTotalCommentCount(); // 총 댓글 수 업데이트
            })
            .catch(error => {
                console.error(error);
                comments = [];
                displayComments(currentPage);
            });
    }

    function fetchTotalCommentCount() {
        fetch('/reviews/count')
            .then(response => {
                if (!response.ok) {
                    throw new Error('리뷰 갯수를 가져오는 중 오류가 발생했습니다: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const totalCountElement = document.getElementById('total-count');
                totalCountElement.textContent = `총 리뷰 갯수: ${data}개`; // 데이터에서 바로 값을 가져오기 때문에 data.count 대신 data를 사용
            })
            .catch(error => {
                console.error(error);
                const totalCountElement = document.getElementById('total-count');
                totalCountElement.textContent = '총 리뷰 갯수: 오류';
            });
    }

    function displayComments(page) {
        const commentSection = document.getElementById('comment-section');
        commentSection.innerHTML = '';

        const table = document.createElement('table');
        table.className = 'comment-table';

        const header = document.createElement('tr');
        header.innerHTML = `
            <th><input type="checkbox" id="select-all"></th>
            <th>번호</th>
            <th>타로주제</th>
            <th>리뷰내용</th>
            <th>작성날짜</th>
            <th>관리</th>
        `;
        table.appendChild(header);

        const startIndex = (page - 1) * commentsPerPage;
        const endIndex = Math.min(startIndex + commentsPerPage, comments.length);

        if (comments.length === 0) {
            const noCommentsRow = document.createElement('tr');
            const noCommentsCell = document.createElement('td');
            noCommentsCell.colSpan = 6;
            noCommentsCell.id = 'no-comments';
            noCommentsCell.textContent = '작성글이 없습니다.';
            noCommentsRow.appendChild(noCommentsCell);
            table.appendChild(noCommentsRow);
        } else {
            for (let i = startIndex; i < endIndex; i++) {
                const comment = comments[i];
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><input type="checkbox" class="select-comment"></td>
                    <td>${comments.length - i}</td>
                    <td>${comment.mainCategoryName || ''}</td>
                    <td>${comment.reviewText || ''}</td>
                    <td>${formatDate(comment.reviewCreate) || ''}</td>
                    <td><button class="edit-button" data-id="${comment.reviewId}" data-text="${comment.reviewText}" data-rating="${comment.rating}">수정</button></td>
                `;
                table.appendChild(row);
            }
        }

        commentSection.appendChild(table);

        renderPagination(comments.length, commentsPerPage, page);

        document.getElementById('select-all').addEventListener('change', function() {
            const checkboxes = document.querySelectorAll('.select-comment');
            for (let checkbox of checkboxes) {
                checkbox.checked = this.checked;
            }
            updateSelectedCount();
            toggleSelectedCountDisplay();
        });

        const checkboxes = document.querySelectorAll('.select-comment');
        for (let checkbox of checkboxes) {
            checkbox.addEventListener('change', function() {
                const selectedCount = document.querySelectorAll('.select-comment:checked').length;
                if (selectedCount > 10) {
                    this.checked = false; // 체크박스 체크 막기
                    return;
                }
                updateSelectedCount();

                if (!this.checked) {
                    document.getElementById('select-all').checked = false;
                } else {
                    const allChecked = Array.from(checkboxes).every(chk => chk.checked);
                    if (allChecked) {
                        document.getElementById('select-all').checked = true;
                    }
                }
                toggleSelectedCountDisplay();
            });
        }

        const modal = document.getElementById('deleteModal');
        const confirmDeleteButton = document.getElementById('confirm-delete');
        const cancelDeleteButton = document.getElementById('cancel-delete');

        document.getElementById('delete-selected').addEventListener('click', function() {
            const selectedCount = document.querySelectorAll('.select-comment:checked').length;
            if (selectedCount > 0) {
                modal.style.display = 'block';
            }
        });

        cancelDeleteButton.onclick = function() {
            modal.style.display = 'none';
        };

        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        };

        confirmDeleteButton.addEventListener('click', function() {
            const selectedComments = document.querySelectorAll('.select-comment:checked');
            selectedComments.forEach(checkbox => {
                const row = checkbox.closest('tr');
                const index = Array.from(row.parentNode.children).indexOf(row) - 1 + startIndex;
                const reviewId = comments[index].reviewId;
                deleteReview(reviewId);
                comments.splice(index, 1);
            });
            modal.style.display = 'none';
            displayComments(currentPage);
        });

        updateSelectedCount();
        toggleSelectedCountDisplay();

        const editButtons = document.querySelectorAll('.edit-button');
        for (let editButton of editButtons) {
            editButton.addEventListener('click', function() {
                const reviewId = this.getAttribute('data-id');
                const reviewText = this.getAttribute('data-text');
                const rating = parseInt(this.getAttribute('data-rating'));
                fillEditModal(reviewId, reviewText, rating);
            });
        }
    }

    function renderPagination(totalComments, commentsPerPage, currentPage) {
        const paginationContainer = document.getElementById('pagination');
        paginationContainer.innerHTML = '';
        const pageCount = Math.ceil(totalComments / commentsPerPage);

        for (let i = 1; i <= pageCount; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.className = 'page-button';
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pageButton.addEventListener('click', () => {
                displayComments(i);
            });
            paginationContainer.appendChild(pageButton);
        }
    }

    function closeModal(modalId) {
        const modal = document.getElementById(modalId);
        modal.style.display = 'none';
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('ko-KR');
    }

function fillEditModal(reviewId, reviewText, rating) {
    const editReviewTextElement = document.getElementById('edit-review-text');
    const editReviewIdElement = document.getElementById('edit-review-id');
    const editModal = document.getElementById('editModal');

    if (editReviewTextElement && editReviewIdElement && editModal) {
        editReviewTextElement.value = reviewText;
        editReviewIdElement.value = reviewId;
        updateStars(rating, '#edit-rating');
        document.getElementById('edit-rating-score').textContent = `${rating}점`;
        document.getElementById('edit-rating-score-value').value = rating;

        editModal.style.display = 'block';

        // 별점 클릭 이벤트 리스너 추가
        const stars = editModal.querySelectorAll('#edit-rating .star');
        stars.forEach((star, index) => {
            star.addEventListener('click', () => {
                const newRating = index + 1;
                updateStars(newRating, '#edit-rating');
                document.getElementById('edit-rating-score').textContent = `${newRating}점`;
                document.getElementById('edit-rating-score-value').value = newRating;

                // 모든 별 초기화 (기본 회색)
                stars.forEach((s, idx) => {
                    s.classList.remove('checked');
                });

                // 클릭된 별까지 색상 변경
                for (let i = 0; i <= index; i++) {
                    stars[i].classList.add('checked');
                }
            });
        });

        // 닫기 및 취소 버튼 이벤트 리스너 추가
        const closeButtons = editModal.getElementsByClassName('close');
        for (let closeButton of closeButtons) {
            closeButton.onclick = function() {
                closeModal('editModal');
            };
        }

        const cancelEditButton = document.getElementById('cancel-edit');
        if (cancelEditButton) {
            cancelEditButton.onclick = function() {
                closeModal('editModal');
            };
        }

        // 수정 폼 제출 이벤트 리스너 추가
        const editForm = document.getElementById('edit-form');
        if (editForm) {
            editForm.addEventListener('submit', function(event) {
                event.preventDefault();
                const updatedReview = {
                    reviewId: parseInt(editReviewIdElement.value),
                    reviewText: editReviewTextElement.value,
                    rating: parseInt(document.getElementById('edit-rating-score-value').value)
                };

                updateReview(updatedReview);
            });
        } else {
            console.error('Edit review modal element not found');
        }
    } else {
        console.error('Edit review modal elements not found');
    }
}


function updateReview(review) {
    fetch(`/reviews/${review.reviewId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(review),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('리뷰 업데이트에 실패했습니다: ' + response.statusText);
        }
        return response.json(); // 이 부분이 누락되어 있었습니다
    })
    .then(data => {
        console.log('리뷰가 성공적으로 업데이트되었습니다:', data);
        closeModal('editModal');
        fetchComments(); // 업데이트 후 댓글 목록 다시 불러오기
    })
    .catch(error => {
        console.error('리뷰 업데이트 중 오류가 발생했습니다:', error);
        closeModal('editModal');
    });
}

    function deleteReview(reviewId) {
        fetch(`/reviews/${reviewId}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('리뷰 삭제에 실패했습니다: ' + response.statusText);
            }
            console.log('리뷰가 성공적으로 삭제되었습니다');
            fetchComments(); // 삭제 후 댓글 목록 다시 불러오기
        })
        .catch(error => {
            console.error('리뷰 삭제 중 오류가 발생했습니다:', error);
        });
    }

    function updateStars(rating, containerSelector) {
        const stars = document.querySelectorAll(`${containerSelector} .star`);
        stars.forEach((star, index) => {
            if (index < rating) {
                star.classList.add('checked');
            } else {
                star.classList.remove('checked');
            }
        });
    }

    function updateSelectedCount() {
        const selectedCount = document.querySelectorAll('.select-comment:checked').length;
        document.getElementById('selected-count').textContent = `${selectedCount}/10 선택`;
    }

    function toggleSelectedCountDisplay() {
        const selectedCount = document.querySelectorAll('.select-comment:checked').length;
        const deleteSelectedButton = document.getElementById('delete-selected');
        if (selectedCount > 0) {
            deleteSelectedButton.style.display = 'inline-block';
        } else {
            deleteSelectedButton.style.display = 'none';
        }
    }

    fetchComments(); // 페이지 로드 시 댓글 목록 불러오기
});

// 별점 클릭 이벤트 리스너 추가 (복사한 부분)
const stars = document.querySelectorAll('#edit-rating .star');
stars.forEach((star, index) => {
    star.addEventListener('click', () => {
        const rating = index + 1; // Rating is 1-indexed
        updateStars(rating, '#edit-rating');
        document.getElementById('edit-rating-score-value').value = rating;
    });
});