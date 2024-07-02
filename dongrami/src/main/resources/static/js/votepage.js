document.addEventListener("DOMContentLoaded", function() {
    const voteListDiv = document.getElementById('vote-list');
    const prevPageButton = document.getElementById('prevPage');
    const nextPageButton = document.getElementById('nextPage');
    const currentPageSpan = document.getElementById('currentPage');
    const pageNumbersDiv = document.getElementById('pageNumbers'); // 페이지 번호를 표시할 div 추가
    
    let previousOption = null;
    let currentPage = 0;
    const pageSize = 3;
    
    function fetchVotes(page) {
        fetch(`/api/votes/paged-votes?page=${page}&size=${pageSize}`)
            .then(response => response.json())
            .then(data => {
                voteListDiv.innerHTML = ''; // Clear existing votes
                data.content.forEach(vote => {
                    const voteDiv = createVoteElement(vote);
                    voteListDiv.appendChild(voteDiv);
                });
                updatePaginationButtons(data);
            })
            .catch(error => console.error('Error fetching votes:', error));
    }

    function updatePaginationButtons(data) {
        currentPageSpan.textContent = data.number + 1;
        prevPageButton.disabled = data.first;
        nextPageButton.disabled = data.last;

        // 페이지 번호 목록 업데이트
        pageNumbersDiv.innerHTML = '';
        for (let i = 0; i < data.totalPages; i++) {
            const pageNumberButton = document.createElement('button');
            pageNumberButton.textContent = i + 1;
            pageNumberButton.addEventListener('click', function() {
                currentPage = i;
                fetchVotes(currentPage);
            });
            if (i === data.number) {
                pageNumberButton.classList.add('current-page');
            }
            pageNumbersDiv.appendChild(pageNumberButton);
        }
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
        barContainer1.id = `barContainer1_${vote.voteId}`; // voteId를 사용하여 고유한 id 생성
        barContainer1.innerHTML = `
            <div class="bar1" id="bar1_${vote.voteId}"></div>
            <span class="text-left" id="text1">A</span>
            <span class="percentage" id="percentage1_${vote.voteId}"></span>
        `;
        voteDiv.appendChild(barContainer1);

        const barContainer2 = document.createElement('div');
        barContainer2.classList.add('bar-container2');
        barContainer2.id = `barContainer2_${vote.voteId}`; // voteId를 사용하여 고유한 id 생성
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

    prevPageButton.addEventListener('click', function() {
        if (currentPage > 0) {
            currentPage--;
            fetchVotes(currentPage);
        }
    });

    nextPageButton.addEventListener('click', function() {
        if (!nextPageButton.disabled) { // 다음 페이지 버튼이 활성화 상태일 때만 실행
            currentPage++;
            fetchVotes(currentPage);
        }
    });

    fetchVotes(currentPage); // 초기 로드
});
