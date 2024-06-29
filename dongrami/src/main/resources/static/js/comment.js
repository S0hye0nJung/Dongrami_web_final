document.addEventListener("DOMContentLoaded", function() {
    const commentListDiv = document.getElementById('comment-list');
    const commentInput = document.getElementById('commentInput');
    const submitCommentButton = document.getElementById('submitComment');
    const urlParams = new URLSearchParams(window.location.search);
    const voteId = urlParams.get('id'); // URL에서 voteId를 가져옵니다.
     function fetchComments(voteId) {
        fetch(`/api/replies/vote/${voteId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch comments');
                }
                return response.json();
            })
            .then(comments => {
                comments.forEach(comment => {
                    const commentElem = createCommentElement(comment);
                    commentDiv.appendChild(commentElem);
                });
            })
            .catch(error => {
                console.error('Error fetching comments:', error);
            });
    }

    function createCommentElement(comment) {
        const commentDiv = document.createElement('div');
        commentDiv.classList.add('comment-item');

        commentDiv.innerHTML = `
            <h4>${comment.member.userId}</h4>
            <p>${comment.content}</p>
            <p>작성일: ${comment.replyCreate}</p>
            <h5></h5>
        `;

        return commentDiv;
    }
    function submitComment() {
        const commentText = commentInput.value;

        if (commentText) {
            // 댓글 데이터를 JSON 형식으로 생성
            const commentData = {
                content: commentText,
                vote: {
                    voteId: voteId
                }
            };

            // Fetch API를 이용해 서버에 POST 요청
            fetch(`/api/comment/${voteId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(commentData)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to post comment');
                }
                return response.json();
            })
            .then(data => {
                // 댓글이 성공적으로 등록되었을 때, 화면에 댓글을 추가
                const newComment = createCommentElement(data);
                commentListDiv.appendChild(newComment);

                // 입력 필드 초기화
                commentInput.value = '';
            })
            .catch(error => {
                console.error('Error posting comment:', error);
            });
        } else {
            alert('Please enter a comment.');
        }
    }

    submitCommentButton.addEventListener('click', submitComment);

    // 페이지 로드 시 기존 댓글 가져오기
    fetchComments(voteId);
});
