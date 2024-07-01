document.addEventListener("DOMContentLoaded", function() {
    const voteListDiv = document.getElementById('vote-list');
    const commentDiv = document.getElementById('comment-section');
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    const voteId = id;
    document.getElementById('voteId').value = voteId;

    // 페이지 로딩 시 투표 정보 가져오기
    fetchVoteById(voteId);

    function fetchVoteById(id) {
        fetch(`/api/votes/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch vote');
                }
                return response.json();
            })
            .then(vote => {
                const voteDiv = createVoteElement(vote);
                voteListDiv.appendChild(voteDiv);
                fetchComments(voteId);
            })
            .catch(error => {
                console.error('Error fetching vote:', error);
            });
    }

    function createVoteElement(vote) {
        const voteDiv = document.createElement('div');
        voteDiv.classList.add('vote-item');

        voteDiv.innerHTML = `
            <h2>${vote.question}</h2>
            <img src="${vote.voteImage}" alt="Vote Image">
            <p>${vote.option1}</p>
            <p>${vote.option2}</p>
        `;
        const buttonContainer = document.createElement('div');
        buttonContainer.classList.add('button-container');

        const button1 = document.createElement('button');
        button1.textContent = '투표';
        button1.addEventListener('click', function() {
            voteOption(vote.voteId, 'option1');
            button1.classList.add('selected');
            button2.classList.remove('selected');
        });

        const button2 = document.createElement('button');
        button2.textContent = '투표';
        button2.addEventListener('click', function() {
            voteOption(vote.voteId, 'option2');
            button2.classList.add('selected');
            button1.classList.remove('selected');
        });

        buttonContainer.appendChild(button1);
        buttonContainer.appendChild(button2);
        voteDiv.appendChild(buttonContainer);

        const barContainer1 = document.createElement('div');
        barContainer1.classList.add('bar-container1');
        barContainer1.id = `barContainer1_${vote.voteId}`;
        barContainer1.innerHTML = `
            <div class="bar1" id="bar1_${vote.voteId}"></div>
            <span class="text-left" id="text1">A</span>
            <span class="percentage" id="percentage1_${vote.voteId}"></span>
        `;
        voteDiv.appendChild(barContainer1);

        const barContainer2 = document.createElement('div');
        barContainer2.classList.add('bar-container2');
        barContainer2.id = `barContainer2_${vote.voteId}`;
        barContainer2.innerHTML = `
            <div class="bar2" id="bar2_${vote.voteId}"></div>
            <span class="text-left" id="text2">B</span>
            <span class="percentage" id="percentage2_${vote.voteId}"></span>
        `;
        voteDiv.appendChild(barContainer2);

        const replyContainer = document.createElement('div');
        replyContainer.classList.add('reply-container');
        const replyButton = document.createElement('button');
        replyButton.id = 'replyButton';
        replyButton.textContent = '반응 보기';
        replyButton.addEventListener('click', function() {
            window.location.href = `/mainvote?id=${vote.voteId}`;
        });
        replyContainer.appendChild(replyButton);
        voteDiv.appendChild(replyContainer);

        return voteDiv;
    }

    window.voteOption = function(voteId, option) {
        fetch(`/api/votes/${voteId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ option: option }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to vote');
            }
            return response.json();
        })
        .then(updatedVote => {
            console.log('Vote successful:', updatedVote);
            updateVoteResults(updatedVote);
        })
        .catch(error => {
            console.error('Error voting:', error);
        });
    };

    function updateVoteResults(vote) {
        const totalVotes = vote.option1Count + vote.option2Count;
        const percentage1 = totalVotes === 0 ? 0 : Math.round((vote.option1Count / totalVotes) * 100);
        const percentage2 = totalVotes === 0 ? 0 : Math.round((vote.option2Count / totalVotes) * 100);

        const percentage1Elem = document.getElementById(`percentage1_${vote.voteId}`);
        const percentage2Elem = document.getElementById(`percentage2_${vote.voteId}`);
        const bar1 = document.getElementById(`bar1_${vote.voteId}`);
        const bar2 = document.getElementById(`bar2_${vote.voteId}`);

        percentage1Elem.textContent = `${percentage1}%`;
        percentage2Elem.textContent = `${percentage2}%`;

        bar1.style.width = `${percentage1}%`;
        bar2.style.width = `${percentage2}%`;
    }

    function fetchComments(voteId) {
        fetch(`/api/replies/vote/${voteId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch comments');
                }
                return response.json();
            })
            .then(comments => {
                commentDiv.innerHTML = '';
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
            <h4>${comment.member.nickname}</h4>
            <p>${comment.content}</p>
            <p>작성일: ${comment.replyCreate}</p>
            <button class="menu-button">...</button>
            <button class="edit-button" style="display:none;">수정</button>
    		<button class="delete-button" style="display:none;">삭제</button>
            <div class="edit-form" style="display:none;">
                <textarea class="edit-content">${comment.content}</textarea>
                <button class="save-edit-button">저장</button>
                <button class="cancel-edit-button">취소</button>
            </div>
        `;
		const menuButton = commentDiv.querySelector('.menu-button');
        const editButton = commentDiv.querySelector('.edit-button');
        const deleteButton = commentDiv.querySelector('.delete-button');
        const editForm = commentDiv.querySelector('.edit-form');
        const saveEditButton = editForm.querySelector('.save-edit-button');
        const cancelEditButton = editForm.querySelector('.cancel-edit-button');
        const editContent = editForm.querySelector('.edit-content');
		menuButton.addEventListener('click', function(){
   			 editButton.style.display = 'block';
    		deleteButton.style.display = 'block';
    	});
        editButton.addEventListener('click', function() {
            editForm.style.display = 'block';
        });

        cancelEditButton.addEventListener('click', function() {
            editForm.style.display = 'none';
        });

        saveEditButton.addEventListener('click', function() {
            const updatedContent = editContent.value;
            updateComment(comment.replyId, updatedContent, commentDiv);
        });

        deleteButton.addEventListener('click', function() {
            deleteComment(comment.replyId, commentDiv);
        });

        return commentDiv;
    }

    function updateComment(replyId, updatedContent, commentElem) {
        fetch(`/api/replies/${replyId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ content: updatedContent }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update comment');
            }
            return response.json();
        })
        .then(updatedComment => {
            commentElem.querySelector('p').textContent = updatedComment.content;
            commentElem.querySelector('.edit-form').style.display = 'none';
        })
        .catch(error => {
            console.error('Error updating comment:', error);
        });
    }

    function deleteComment(replyId, commentElem) {
        fetch(`/api/replies/${replyId}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete comment');
            }
            commentElem.remove();
        })
        .catch(error => {
            console.error('Error deleting comment:', error);
        });
    }

    document.getElementById('replyForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const content = document.getElementById('content').value;
        const level = document.getElementById('level').value;
        const userId = document.getElementById('userId').value;
        const userNickname = document.getElementById('userNicknameInput').value;

        const replyData = {
            content: content,
            level: level,
            member: userId,
            parentReId: null,
            replyCreate: new Date().toISOString(),
            replyModify: new Date().toISOString(),
            voteId: voteId,
        };

        fetch(`/api/replies/${voteId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(replyData)
        })
        .then(response => response.json())
        .then(data => {
			document.getElementById('content').value = '';  // 댓글 작성 후 content 필드 초기화
            fetchComments(voteId);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    });

});
