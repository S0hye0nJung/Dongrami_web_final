document.addEventListener("DOMContentLoaded", function() {
    const cards = document.querySelectorAll('.tarot-card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = 1;
            card.style.transform = card.style.transform.replace('translateY(50px)', 'translateY(0)');
        }, index * 500); // 각 카드가 0.5초 간격으로 나타남
    });
});