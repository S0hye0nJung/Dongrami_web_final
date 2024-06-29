document.addEventListener('DOMContentLoaded', () => {
    const bubbleTexts = document.querySelectorAll('.bubbleText');

    fetch('/getBubbleData')
        .then(response => response.json())
        .then(data => {
            bubbleTexts.forEach((bubbleText, index) => {
                if (data.length > index) {
                    bubbleText.textContent = data[index].bubble_slack_name;
                } else {
                    bubbleText.textContent = ''; // 데이터가 없을 경우 빈 문자열로 설정
                }
            });
        })
        .catch(error => {
            console.error('Bubble Data 가져오기 오류:', error);
        });
});