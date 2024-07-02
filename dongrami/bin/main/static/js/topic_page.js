document.addEventListener("DOMContentLoaded", function() {
    const cards = document.querySelectorAll(".tarot-card");
    const cardContainer = document.querySelector(".card-container");
    const totalCards = cards.length;
    const startAngle = 0; // Start angle (3 o'clock)
    const endAngle = 180; // End angle (9 o'clock)
    const radius = 300; // Radius (distance from center to card)

    cards.forEach((card, index) => {
        const angle = startAngle + (endAngle - startAngle) * (index / (totalCards - 1));
        const angleInRadians = angle * (Math.PI / 180);
        const x = radius * Math.cos(angleInRadians) - card.offsetWidth / 2;
        const y = radius * Math.sin(angleInRadians) - card.offsetHeight;

        setTimeout(() => {
            card.style.transform = `translate(${x}px, ${y}px) rotate(${angle - 90}deg)`;
            card.style.opacity = 1;
            // 배경색 변경 애니메이션
            const bgColor = 255 - Math.floor(255 * (index / totalCards)); // 적절한 색깔
            cardContainer.style.backgroundColor = `rgb(${bgColor}, ${bgColor}, ${bgColor})`; // 색상 변경
        }, index * 100); // 애니메이션을 각 카드마다 일정한 시간으로 분리
    });
});