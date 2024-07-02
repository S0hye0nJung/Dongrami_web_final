document.addEventListener('DOMContentLoaded', () => {
    const reviewContainer = document.getElementById('reviews');
    const reviewCountElement = document.getElementById('review-count');
    const ratingValueElement = document.querySelector('.rating-value');
    const ratingDetailsElement = document.querySelector('.rating-details');
    const starElements = document.querySelectorAll('.stars .star');
    const paginationContainer = document.getElementById('pagination');

    const reviewsPerPage = 10;
    let currentPage = 1;

    // 기존 리뷰 데이터
    const reviews = [
        {
            image: "assets/images/love.png", // 이미지 URL을 실제 이미지로 교체하세요
            title: '#연애운',
            text: '성향에 대해 정말 정확히 맞췄어요! 참고해서 꼭 만날게요.. 평소에 사주를 배우자운 들어오는 시기가 비슷하기도 한데 그때 만나서 결혼하면 애를 낳을 수 있을지 걱정이네요 ㅎㅎㅎ',
            stars: 5,
            date: '2023.06.01'
        },
        // 추가 리뷰 데이터
    ];

    // 새로운 리뷰 추가
    const newReviewText = localStorage.getItem('reviewText');
    const newRatingScore = localStorage.getItem('ratingScore');

    if (newReviewText && newRatingScore) {
        const newReview = {
            image: "assets/images/default.png", // 기본 이미지
            title: `#${localStorage.getItem('subject')}`,
            text: newReviewText,
            stars: parseInt(newRatingScore),
            date: new Date().toISOString().split('T')[0]
        };
        reviews.push(newReview);
        localStorage.removeItem('reviewText');
        localStorage.removeItem('ratingScore');
        localStorage.removeItem('subject'); // 추가된 부분: 리뷰 주제 제거
    }

    function renderReviews(page) {
        reviewContainer.innerHTML = '';
        const start = (page - 1) * reviewsPerPage;
        const end = start + reviewsPerPage;
        const pageReviews = reviews.slice(start, end);

        pageReviews.forEach(review => {
            const reviewCard = document.createElement('div');
            reviewCard.className = 'review-card';
            reviewCard.innerHTML = `
                <button class="next-button">&gt;</button>
                <img src="${review.image}" alt="Review Image" class="review-image">
                <div class="review-content">
                    <div class="review-header">
                        <div class="review-title">${review.title}</div>
                        <div class="review-meta">
                            <div class="stars">
                                ${generateStars(review.stars)}
                            </div>
                            <div class="review-separator">|</div>
                            <div class="review-date">${review.date}</div>
                        </div>
                    </div>
                    <div class="review-divider"></div>
                    <p class="review-text">${review.text}</p>
                </div>
                <div class="nickname">${review.nickname || '익명'}</div>
            `;
            reviewContainer.appendChild(reviewCard);
        });

        // 리뷰 개수와 평균 별점 업데이트
        const totalReviews = reviews.length;
        const totalStars = reviews.reduce((acc, review) => acc + review.stars, 0);
        const averageRating = (totalStars / totalReviews).toFixed(1);

        reviewCountElement.textContent = totalReviews;
        ratingValueElement.textContent = averageRating;
        ratingDetailsElement.textContent = `${totalReviews}개의 리뷰`;

        // 별 색칠하기
        const filledStars = Math.round(averageRating);
        starElements.forEach((star, index) => {
            if (index < filledStars) {
                star.classList.add('filled');
            } else {
                star.classList.remove('filled');
            }
        });

        renderPagination(totalReviews);
    }

    function generateStars(stars) {
        let starHTML = '';
        for (let i = 0; i < 5; i++) {
            if (i < stars) {
                starHTML += '<span class="star filled">★</span>';
            } else {
                starHTML += '<span class="star">★</span>';
            }
        }
        return starHTML;
    }

    function renderPagination(totalReviews) {
        paginationContainer.innerHTML = '';
        const pageCount = Math.ceil(totalReviews / reviewsPerPage);

        for (let i = 1; i <= pageCount; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.className = 'page-button';
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pageButton.addEventListener('click', () => {
                currentPage = i;
                renderReviews(currentPage);
            });
            paginationContainer.appendChild(pageButton);
        }
    }

    renderReviews(currentPage);

    // 버튼 클릭 시 다른 페이지로 이동
    document.querySelectorAll('.next-button').forEach(button => {
        button.addEventListener('click', () => {
            window.location.href = '다른페이지.html'; // 원하는 페이지 URL로 변경
        });
    });
});
