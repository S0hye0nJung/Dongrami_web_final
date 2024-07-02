document.addEventListener('DOMContentLoaded', () => {
    fetch('/getBubbleData')
        .then(response => response.json())
        .then(data => {
            const svgItems = document.querySelectorAll('.item');
            const bubbleTexts = document.querySelectorAll('.bubbleText');

            // 데이터를 count 기준으로 내림차순 정렬합니다.
            data.sort((a, b) => b.count - a.count);

            // 각 순위별로 데이터를 설정합니다.
            svgItems.forEach((svgItem, index) => {
                if (data[index]) {
                    const subcategoryId = data[index].subcategory_id;
                    const bubbleSlackName = data[index].bubble_slack_name;

                    // SVG 아이템에 subcategory_id를 설정합니다.
                    svgItem.setAttribute('data-subcategory-id', subcategoryId);

                    // 버블 텍스트에 bubble_slack_name을 설정합니다.
                    bubbleTexts[index].textContent = bubbleSlackName;
                } else {
                    // 데이터가 없을 경우 빈 문자열로 설정합니다.
                    svgItem.setAttribute('data-subcategory-id', '');
                    bubbleTexts[index].textContent = '';
                }
            });

            // 클릭 이벤트 추가
            svgItems.forEach(item => {
                item.addEventListener('click', event => {
                    const subcategoryId = event.currentTarget.dataset.subcategoryId;
                    if (subcategoryId) {
                        window.location.href = `/tarot?subcategory_id=${subcategoryId}`;
                    }
                });
            });
        })
        .catch(error => {
            console.error('Bubble Data 가져오기 오류:', error);
        });
});
