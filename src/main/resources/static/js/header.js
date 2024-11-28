document.addEventListener('DOMContentLoaded', () => {
    const menuBtn = document.querySelector('.menu-btn');
    const menuBar = document.querySelector('.menu-bar');

    let isMenuOpen = false;

    menuBtn.addEventListener('click', () => {
        if (!isMenuOpen) {
            // 메뉴 열기
            gsap.to(menuBar, { duration: 0.5, x: '0%' });
            isMenuOpen = true;
        } else {
            // 메뉴 닫기
            gsap.to(menuBar, { duration: 0.5, x: '100%' });
            isMenuOpen = false;
        }
    });
});