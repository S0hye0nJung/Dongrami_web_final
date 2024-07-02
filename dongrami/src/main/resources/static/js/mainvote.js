document.addEventListener("DOMContentLoaded", function() {
    const voteListDiv = document.getElementById('vote-list');
    const commentDiv = document.getElementById('comment-section');
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    const voteId = id;
    let previousOption = null;
    document.getElementById('voteId').value = voteId;
    
    let currentPage;  // 현재 페이지를 나타내는 변수 추가
	const pageSize = 5;  // 페이지 당 댓글 수 설정 (예시로 10개씩)

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
        button1.classList.add('button1');
        button1.textContent = '투표';
        button1.addEventListener('click', function() {
            voteOption(vote.voteId, 'option1');
            button1.classList.add('selected');
            button2.classList.remove('selected');
        });

        const button2 = document.createElement('button');
        button2.classList.add('button2');
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

        return voteDiv;
    }

     window.voteOption = function(voteId, option) {
    fetch(`/api/votes/${voteId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ option: option, previousOption: previousOption }),
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
        previousOption = option; // 현재 선택을 이전 선택으로 저장
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
	//댓글 조회
function fetchComments(voteId, page = 0, size = 5) {
    fetch(`/api/replies/${voteId}/replies?page=${page}&size=${size}&sort=replyCreate,desc`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch comments');
            }
            return response.json();
        })
        .then(data => {
            const comments = data.content; // Page 객체의 content 속성에 실제 댓글들이 있습니다.
            const commentDiv = document.getElementById('comment-section'); // `commentDiv`가 정의된 곳에 맞게 수정
            commentDiv.innerHTML = '';
            comments.forEach(comment => {
                const commentElem = createCommentElement(comment);
                commentDiv.appendChild(commentElem);
            });

            // 페이지 네이션 UI 업데이트
            updatePagination(data, voteId, size);
        })
        .catch(error => {
            console.error('Error fetching comments:', error);
        });
}

function updatePagination(data, voteId, size) {
    const paginationDiv = document.getElementById('pagination');
    paginationDiv.innerHTML = '';

    // 이전 페이지 버튼
    const prevButton = document.createElement('button');
    prevButton.textContent = '< 이전';
    prevButton.onclick = () => fetchComments(voteId, data.number - 1, size);
    prevButton.setAttribute('id', 'prevButton'); // id 부여
    paginationDiv.appendChild(prevButton);

    // 페이지 번호 버튼
    let startPage = Math.max(0, data.number - 1); // 시작 페이지 번호 계산
    let endPage = startPage + 2; // 보여줄 페이지 버튼의 끝 번호 계산
    if (endPage > data.totalPages - 1) {
        endPage = data.totalPages - 1;
    }
    startPage = Math.max(0, endPage - 2); // 시작 페이지 번호 다시 계산

    for (let i = startPage; i <= endPage; i++) {
        const pageNumberButton = document.createElement('button');
        pageNumberButton.textContent = i + 1;
        pageNumberButton.addEventListener('click', function() {
            fetchComments(voteId, i, size); // 페이지 번호를 인자로 하여 fetchComments 호출
        });

        if (i === data.number) {
            pageNumberButton.classList.add('current-page'); // 현재 페이지에 해당하는 버튼에 클래스 추가
        }

        paginationDiv.appendChild(pageNumberButton);
    }

    // 다음 페이지 버튼
    
      if (!data.last) {
    const nextButton = document.createElement('button');
    nextButton.textContent = '다음 >';
    nextButton.onclick = () => fetchComments(voteId, data.number + 1, size);
    nextButton.setAttribute('id', 'nextButton'); // id 부여
    paginationDiv.appendChild(nextButton);
} else {
    const nextButton = document.getElementById('nextButton');
    if (nextButton) {
        paginationDiv.removeChild(nextButton); // 버튼 제거
    }
}
    
}



    function createCommentElement(comment) {
        const commentDiv = document.createElement('div');
        commentDiv.classList.add('comment-item');

        commentDiv.innerHTML = `
            <h4>${comment.member.nickname}</h4>
            <p>${comment.content}</p>
            <h5>${comment.replyCreate}</h5>
            <button class="menu-button">...</button>
            <button class="edit-button" style="display:none;">수정</button>
    		<button class="delete-button" style="display:none;">삭제</button>
    		<h6></h6>
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
		menuButton.addEventListener('click', function() {
    		// 현재 display 상태를 확인하고 토글
   		 	if (editButton.style.display === 'none' && deleteButton.style.display === 'none') {
        			editButton.style.display = 'block';
        			deleteButton.style.display = 'block';
    		} 	else {
        			editButton.style.display = 'none';
        			deleteButton.style.display = 'none';
        			editForm.style.display = 'none';
    		}
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
		 const currentDate = new Date().toISOString();  // 현재 시간을 ISO 포맷으로 가져옴
        fetch(`/api/replies/${replyId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ content: updatedContent, replyModify: currentDate }),
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
