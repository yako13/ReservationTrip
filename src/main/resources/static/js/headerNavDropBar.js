
const allDropdowns = document.querySelectorAll('.megaDropdown');
let hideTimeout;
let openTimeout;

document.querySelectorAll('.category-toggle').forEach(toggle => {
    const dropdownId = toggle.dataset.target;
    const dropdown = document.getElementById(dropdownId);
    if (!dropdown || dropdown.innerHTML.trim() === '') return;

    toggle.addEventListener('mouseenter', () => {
        clearTimeout(hideTimeout);
        clearTimeout(openTimeout);

        // 먼저 모두 닫고
        hideAllDropdownsExcept(null);

        // 💡 새 드롭다운은 딜레이 후 열기
        openTimeout = setTimeout(() => {
            hideAllDropdownsExcept(dropdown); // 다른 거 다시 닫고
            dropdown.classList.add('show');
        }, 350); // ← 이게 핵심 delay (200~300ms 정도 추천)
    });

    toggle.addEventListener('mouseleave', () => {
        clearTimeout(openTimeout);
        hideTimeout = setTimeout(() => dropdown.classList.remove('show'), 300);
    });

    dropdown.addEventListener('mouseenter', () => {
        clearTimeout(hideTimeout);
        clearTimeout(openTimeout);
    });

    dropdown.addEventListener('mouseleave', () => {
        hideTimeout = setTimeout(() => dropdown.classList.remove('show'), 300);
    });
});

window.addEventListener('wheel', () => {
    hideAllDropdownsExcept(null); // 전체 닫기
});

function hideAllDropdownsExcept(current) {
    allDropdowns.forEach(d => {
        if (d !== current) d.classList.remove('show');
    });




}
