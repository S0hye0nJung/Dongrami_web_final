    document.addEventListener('DOMContentLoaded', () => {
      const nodes = document.querySelectorAll('.node');

      function getRandomPosition() {
        const x = Math.random() * 300; // 0 ~ 300 사이의 랜덤 X 좌표
        const y = Math.random() * 200; // 0 ~ 200 사이의 랜덤 Y 좌표
        return { x, y };
      }

      function setRandomPosition(node) {
        const position = getRandomPosition();
        node.style.transform = `translate(${position.x}px, ${position.y}px)`;
      }

      nodes.forEach(node => setRandomPosition(node));
    });