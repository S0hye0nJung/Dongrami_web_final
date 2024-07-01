const groups = [
    createGroup(["option1", "option2"], { option1: 0, option2: 0 }, ["bar1", "bar2"], ["percentage1", "percentage2"], "toggleButton1", "comment-container1", "replyButton1"),
    createGroup(["option3", "option4"], { option3: 0, option4: 0 }, ["bar3", "bar4"], ["percentage3", "percentage4"], "toggleButton2", "comment-container2", "replyButton2"),
    createGroup(["option5", "option6"], { option5: 0, option6: 0 }, ["bar5", "bar6"], ["percentage5", "percentage6"], "toggleButton3", "comment-container3", "replyButton3")
  ];
  
  function createGroup(options, votes, barIds, percentageIds, toggleButtonId, commentContainerId, replyButtonId) {
    return { options, votes, barIds, percentageIds, toggleButtonId, commentContainerId, replyButtonId, selectedOption: null };
  }
  
  function vote(option) {
    let groupIndex, optionIndex;
  
    if (option === 1 || option === 2) {
      groupIndex = 0;
    } else if (option === 3 || option === 4) {
      groupIndex = 1;
    } else if (option === 5 || option === 6) {
      groupIndex = 2;
    }
    const group = groups[groupIndex];
    optionIndex = (option - 1) % 2;
  
    if (group.selectedOption !== null) {
      group.votes[group.options[group.selectedOption]]--;
      document.getElementById(`voteButton${group.selectedOption + 1}`).style.backgroundColor = "";
      document.getElementById(`voteButton${group.selectedOption + 3}`).style.backgroundColor = "";
      document.getElementById(`voteButton${group.selectedOption + 5}`).style.backgroundColor = "";
    }
    
    group.votes[group.options[optionIndex]]++;
    group.selectedOption = optionIndex;
  
    document.getElementById(`voteButton${option}`).style.backgroundColor = "gray";
  
    updateResults(groupIndex);
  }
  
  function updateResults(groupIndex) {
    const group = groups[groupIndex];
    const totalVotes = group.votes[group.options[0]] + group.votes[group.options[1]];
    for (let i = 0; i < 2; i++) {
      const percentage = totalVotes ? Math.round((group.votes[group.options[i]] / totalVotes) * 100) : 0;
      document.getElementById(group.barIds[i]).style.width = percentage + "%";
      document.getElementById(group.percentageIds[i]).innerText = percentage + "%";
    }
    toggleButtonVisibility(groupIndex);
    replyButtonVisibility(groupIndex);
  }
  
  document.addEventListener("DOMContentLoaded", function() {
    groups.forEach(group => {
      const toggleButton = document.getElementById(group.toggleButtonId);
      toggleButton.style.display = "none";
      const commentSection = document.getElementById(group.commentContainerId);
      commentSection.style.display = "none";
      const replyButton = document.getElementById(group.replyButtonId);
      replyButton.style.display = "none";
    });
  });
  
  function toggleButtonVisibility(groupIndex) {
    const toggleButton = document.getElementById(groups[groupIndex].toggleButtonId);
    toggleButton.style.display = "block";
  }
  
  function replyButtonVisibility(groupIndex) {
    const replyButton = document.getElementById(groups[groupIndex].replyButtonId);
    replyButton.style.display = "block";
  }
  
  
  document.addEventListener("DOMContentLoaded", function() {
    groups.forEach(group => {
        const toggleButton = document.getElementById(group.toggleButtonId);
        toggleButton.style.display = "none";
        const commentSection = document.getElementById(group.commentContainerId);
        commentSection.style.display = "none";
        const replyButton = document.getElementById(group.replyButtonId);
        replyButton.style.display = "none";
    });
  });
  
  function toggleButtonVisibility(groupIndex) {
    const toggleButton = document.getElementById(groups[groupIndex].toggleButtonId);
    toggleButton.style.display = "block";
  }
  function replyButtonVisibility(groupIndex) {
    const replyButton = document.getElementById(groups[groupIndex].replyButtonId);
    replyButton.style.display = "block";
  }
  
  function toggleComment(commentId, toggleButtonId) {
    const button = document.getElementById(toggleButtonId);
    const commentSection = document.getElementById(commentId);
    if (commentSection.style.display === "none") {
        commentSection.style.display = "block";
        button.innerText = "댓글 숨기기";
    } else {
        commentSection.style.display = "none";
        button.innerText = "댓글 달기";
    }
  }
  
  
  
  
  
  const comments1 = [];
  const comments2 = [];
  const commentsPerPage = 5;
  let currentPage1 = 1;
  let currentPage2 = 1;
  
  function addComment(inputId, listId) {
    const commentInput = document.getElementById(inputId);
    if (commentInput.value.trim() === "") {
        alert("댓글을 입력하세요.");
        return;
    }
  
    const commentText = commentInput.value;
    const now = new Date();
    const commentTime = `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}-${now.getDate().toString().padStart(2, '0')} ${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`;
  
    const comments = (listId === 'comment-list1') ? comments1 : comments2;
    comments.push({ text: commentText, time: commentTime });
    commentInput.value = "";
  
    renderComments(listId);
  }
  
  function renderComments(listId) {
    const commentList = document.getElementById(listId);
    commentList.innerHTML = "";
  
    const comments = (listId === 'comment-list1') ? comments1 : comments2;
    const currentPage = (listId === 'comment-list1') ? currentPage1 : currentPage2;
    const startIndex = (currentPage - 1) * commentsPerPage;
    const endIndex = Math.min(startIndex + commentsPerPage, comments.length);
  
    for (let i = startIndex; i < endIndex; i++) {
        const comment = comments[i];
        const commentElement = document.createElement('div');
        commentElement.className = 'comment';
  
        commentElement.innerHTML = `
            <div class="comment-info">
                <div class="comment-author">익명</div>
                <div class="comment-time">${comment.time}</div>
                <div class="comment-text">${comment.text}</div>
            </div>
            <div class="comment-actions">
                <button class="edit-button" onclick="editComment(this, '${listId}')">수정</button>
                <button class="delete-button" onclick="deleteComment(this, '${listId}')">삭제</button>
            </div>
            <button class="menu-button" onclick="toggleActions(event, this)">...</button>
        `;
        commentList.appendChild(commentElement);
    }
    renderPagination(listId);
  }
  
  function renderPagination(listId) {
    const paginationContainer = document.getElementById(`pagination${listId.charAt(listId.length - 1)}`);
    paginationContainer.innerHTML = "";
  
    const comments = (listId === 'comment-list1') ? comments1 : comments2;
    const currentPage = (listId === 'comment-list1') ? currentPage1 : currentPage2;
    const totalPages = Math.ceil(comments.length / commentsPerPage);
  
    for (let i = 1; i <= totalPages; i++) {
        const button = document.createElement('button');
        button.textContent = i;
        button.className = (i === currentPage) ? 'active' : '';
        button.addEventListener('click', () => {
            if (listId === 'comment-list1') {
                currentPage1 = i;
            } else {
                currentPage2 = i;
            }
            renderComments(listId);
        });
        paginationContainer.appendChild(button);
    }
  }
  
  function editComment(button, listId) {
    const commentElement = button.closest('.comment');
    const commentTextElement = commentElement.querySelector('.comment-text');
    const currentText = commentTextElement.textContent;
  
    const editableDiv = document.createElement('div');
    editableDiv.contentEditable = 'true';
    editableDiv.classList.add('editable-div');
    editableDiv.textContent = currentText;
  
    commentTextElement.style.display = 'none';
    commentTextElement.parentNode.insertBefore(editableDiv, commentTextElement.nextSibling);
  
    const saveButton = document.createElement('button');
    saveButton.textContent = '저장';
    saveButton.classList.add('save-button');
    commentTextElement.parentNode.insertBefore(saveButton, editableDiv.nextSibling);
  
    saveButton.addEventListener('click', () => {
        const newText = editableDiv.textContent.trim();
        if (newText !== "") {
            commentTextElement.textContent = newText;
        }
        editableDiv.remove();
        saveButton.remove();
        commentTextElement.style.display = '';
    });
  }
  
  function deleteComment(button, listId) {
    if (confirm("댓글을 삭제하시겠습니까?")) {
        const commentElement = button.closest('.comment');
        const commentIndex = Array.from(commentElement.parentNode.children).indexOf(commentElement);
        const comments = (listId === 'comment-list1') ? comments1 : comments2;
        comments.splice(commentIndex + ((listId === 'comment-list1' ? currentPage1 : currentPage2 - 1) * commentsPerPage), 1);
        renderComments(listId);
    }
  }
  
  function toggleActions(event, button) {
    event.stopPropagation();
    const commentElement = button.closest('.comment');
    const actionsElement = commentElement.querySelector('.comment-actions');
    if (actionsElement.style.display === 'none' || actionsElement.style.display === '') {
        const allActions = document.querySelectorAll('.comment-actions');
        allActions.forEach(function(actions) {
            actions.style.display = 'none';
        });
        actionsElement.style.display = 'flex';
    } else {
        actionsElement.style.display = 'none';
    }
  }
  
  window.onclick = function(event) {
    if (!event.target.matches('.menu-button')) {
        const actionsElements = document.querySelectorAll('.comment-actions');
        actionsElements.forEach(function(actionsElement) {
            actionsElement.style.display = 'none';
        });
    }
  }
  