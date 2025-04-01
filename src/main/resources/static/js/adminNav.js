function toggleDropdown(button) {
    let dropdown = button.parentElement;
    let content = dropdown.querySelector(".dropdown-content");

    if (dropdown.classList.contains("active")) {
        content.style.maxHeight = "0px";
        content.style.opacity = "0";
        setTimeout(() => {
            dropdown.classList.remove("active");
        }, 300); // transition 시간 후 active 제거
    } else {
        dropdown.classList.add("active");
        content.style.maxHeight = content.scrollHeight + "px"; // 실제 높이로 설정
        content.style.opacity = "1";
    }
}
window.addEventListener('DOMContentLoaded', (event) => {
    openNav();
});

function openNav() {

    document.getElementById("mySidenav").style.width = "200px";
    document.getElementById("main").style.marginLeft = "200px";
    document.querySelector(".SlideNavOpenDiv").classList.remove("SlideShow");
}


function closeNav() {

    document.getElementById("mySidenav").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
    setTimeout(delay, 300);
}

function delay() {
    document.querySelector(".SlideNavOpenDiv").classList.add("SlideShow");
}

if (document.title.includes("주문")) {
    document.getElementById("barMenuCheckout").style.backgroundColor = "#7d8597";
}

if (document.title.includes("매출")) {
    document.getElementById("barMenuSales").style.backgroundColor = "#7d8597";
}

if (document.title.includes("상품")) {
    document.getElementById("barMenuProduct").style.backgroundColor = "#7d8597";
}
