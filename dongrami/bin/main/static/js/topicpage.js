document.addEventListener('DOMContentLoaded', () => {
    const links = document.querySelectorAll('.content-text a');


    // 클릭 수 초기화 및 정렬을 서버 데이터 기준으로 처리
    links.forEach(link => {
        const subcategoryId = link.dataset.subcategoryId;
        $.ajax({
            type: 'GET',
            url: `/getSubcategory/${subcategoryId}`, // 서버에서 해당 서브카테고리의 정보를 가져오는 엔드포인트
            success: function(response) {
                const clickCount = response.count || 0;
                link.dataset.clickCount = clickCount;
                sortLinks(); // 데이터를 받은 후 링크 정렬
            },
            error: function(error) {
                console.log('Error fetching subcategory info', error);
            }
        });
    });


    // 클릭 수를 증가시키는 이벤트 리스너
    links.forEach(link => {
        link.addEventListener('click', (event) => {
            //event.preventDefault(); // 링크의 기본 동작 방지
            const id = link.getAttribute('id');

            let clickCount = parseInt(link.dataset.clickCount) + 1; // 클릭 수 1 증가

            localStorage.setItem(id, clickCount);
            link.dataset.clickCount = clickCount;

            // 클릭 후 링크 정렬
            sortLinks();


            // 서버로 데이터 전송
            const subcategoryId = link.dataset.subcategoryId;
            $.ajax({
                type: 'POST',
                url: '/updateClickCount',
                data: JSON.stringify({ subcategoryId: subcategoryId, count: clickCount }),
                contentType: 'application/json',
                success: function(response) {
                    console.log('Click count updated');
                },
                error: function(error) {
                    console.log('Error updating click count', error);
                }
            });
        });
    });

    // 링크를 클릭 수에 따라 정렬하는 함수
    const sortLinks = () => {
        const sortedLinks = Array.from(links).sort((a, b) => {
            return b.dataset.clickCount - a.dataset.clickCount;
        });
        const container = document.querySelector('.content-text');
        container.innerHTML = '';
        sortedLinks.forEach(link => container.appendChild(link));
    };

    // 초기 링크 정렬
    sortLinks();
});

