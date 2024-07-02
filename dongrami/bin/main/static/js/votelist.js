document.addEventListener("DOMContentLoaded", function() {
    const voteListDiv = document.getElementById('vote-list');

    // 서버에서 투표 리스트 가져오기
    fetch('/api/votes')
        .then(response => response.json())
        .then(data => {
            data.forEach(vote => {
                const voteDiv = createVoteElement(vote);
                voteListDiv.appendChild(voteDiv);
            });
        })
        .catch(error => console.error('Error fetching votes:', error));

    // Vote 엘리먼트 생성 함수
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
   		barContainer1.classList.add('bar-container');
    	barContainer1.id = `barContainer1`; // voteId를 사용하여 고유한 id 생성
    	barContainer1.innerHTML = `
        	<div class="bar1" id="bar1_${vote.voteId}"></div>
        	<span class="text-left" id="text1">A</span>
        	<span class="percentage" id="percentage1_${vote.voteId}"></span>
    		`;
    	voteDiv.appendChild(barContainer1);

        const barContainer2 = document.createElement('div');
    	barContainer2.classList.add('bar-container');
    	barContainer2.id = `barContainer2`; // voteId를 사용하여 고유한 id 생성
    	barContainer2.innerHTML = `
        	<div class="bar2" id="bar2_${vote.voteId}"></div>
        	<span class="text-left" id="text2">B</span>
        	<span class="percentage" id="percentage2_${vote.voteId}"></span>
    	`;
        voteDiv.appendChild(barContainer2);

                const replyContainer = document.createElement('div');
                replyContainer.classList.add('reply-container');
                replyContainer.innerHTML = `
                    <button id="replyButton">반응 보기</button>
                `;
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

    // 각 투표 항목의 고유한 id를 사용하여 업데이트
    const percentage1Elem = document.getElementById(`percentage1_${vote.voteId}`);
    const percentage2Elem = document.getElementById(`percentage2_${vote.voteId}`);
    const bar1 = document.getElementById(`bar1_${vote.voteId}`);
    const bar2 = document.getElementById(`bar2_${vote.voteId}`);

    percentage1Elem.textContent = `${percentage1}%`;
    percentage2Elem.textContent = `${percentage2}%`;

    bar1.style.width = `${percentage1}%`;
    bar2.style.width = `${percentage2}%`;
}
const replyButton = replyContainer.querySelector('#replyButton');
replyButton.addEventListener('click', function() {
    window.location.href = '/mainvote.html'; // 이동할 페이지 경로
});
});
