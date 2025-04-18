

document.addEventListener("DOMContentLoaded", function () {
    openNav();
    activateDropdown();
    initNotificationUI();
    connectWebSocket();
    fetchNotifications();
});

// API로 알림 목록 요청
function fetchNotifications() {
    fetch("/api/notification")
        .then(response => response.json())
        .then(data => {
            const count = data.totalNonReadCount;
            const notifications = data.notificationResponseDtoList;

            updateNotificationBadge(count);
            updateNotificationList(notifications);
        })
        .catch(error => {
            console.error("알림 불러오기 실패:", error);
        });
}

// 사이드 네비 열기
function openNav() {
    document.getElementById("mySidenav").style.width = "200px";
    document.getElementById("main").style.marginLeft = "200px";
    document.querySelector(".SlideNavOpenDiv").classList.remove("SlideShow");
}

// 사이드 네비 닫기
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
    setTimeout(delay, 300);
}

function delay() {
    document.querySelector(".SlideNavOpenDiv").classList.add("SlideShow");
}

// 드롭다운 메뉴 토글
function toggleDropdown(button) {
    const dropdown = button.parentElement;
    const content = dropdown.querySelector(".dropdown-content");

    if (dropdown.classList.contains("active")) {
        content.style.maxHeight = "0px";
        content.style.opacity = "0";
        setTimeout(() => dropdown.classList.remove("active"), 300);
    } else {
        dropdown.classList.add("active");
        content.style.maxHeight = content.scrollHeight + "px";
        content.style.opacity = "1";
    }
}

// 현재 페이지 제목에 따라 드롭다운 자동 펼침
function activateDropdown() {
    const title = document.title;
    let targetDropdownId = null;

    if (title.includes("상품")) {
        targetDropdownId = "productDropdown";
    } else if (title.includes("예약")) {
        targetDropdownId = "reservationDropdown";
    } else if (title.includes("매출")) {
        targetDropdownId = "salesDropdown";
    }

    if (targetDropdownId) {
        const dropdown = document.getElementById(targetDropdownId);
        const content = dropdown.querySelector(".dropdown-content");
        dropdown.classList.add("active");
        content.style.maxHeight = content.scrollHeight + "px";
        content.style.opacity = "1";
    }
}

const badge = document.getElementById("notification-badge");
const listItems = document.getElementById("notification-items");
const bell = document.getElementById("bell");
const listContainer = document.getElementById("notification-list");
const closeIcon = document.getElementById("closeIcon");
const clearBtn = document.getElementById("clear-notifications");

// 알림 기능 초기화
function initNotificationUI() {


    // 알림 아이콘 클릭 시 알림 불러오기
    bell.addEventListener("click", () => {
        fetchNotifications(); // 알림 불러오고
        listContainer.classList.remove("hidden"); // 알림창 열기
        listContainer.style.display = "block";
    });

    // X 버튼 누르면 알림 목록 닫기
    closeIcon.addEventListener("click", () => {
        listContainer.classList.add("hidden");
        listContainer.style.display = "none";
    });

    // 초기화 버튼 클릭 시 알림 목록 및 배지 초기화
    clearBtn.addEventListener("click", () => {
        markAllNotificationsAsRead();

    });



    function markAllNotificationsAsRead() {
        // Ajax 요청을 보내어 알림을 읽음 처리
        fetch("/api/notification/markAllAsRead", {
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    // 알림 목록 초기화 및 UI 업데이트
                    const liCount = listItems.getElementsByTagName("li").length;

                    if (liCount > 0) {
                        listItems.innerHTML = "";
                        const emptyItem = document.createElement("li");
                        emptyItem.innerText = "알림이 없습니다.";
                        listItems.appendChild(emptyItem);

                    }

                    badge.innerText = "";
                    badge.style.display = "none";
                } else {
                    alert("알림 읽음 처리 실패");
                }
            })
            .catch(error => {
                console.error("알림 읽음 처리 실패:", error);
            });
    }




}
// 알림 개수 뱃지 업데이트
function updateNotificationBadge(count) {
    if (count > 0) {
        badge.innerText = `${count}건`;
        badge.style.display = "block";
    } else {
        badge.style.display = "none";
    }
}
// 알림 목록 UI 업데이트
function updateNotificationList(notifications) {
    listItems.innerHTML = "";

    if (!notifications || notifications.length === 0) {
        const emptyItem = document.createElement("li");
        emptyItem.innerText = "알림이 없습니다.";
        listItems.appendChild(emptyItem);
    } else {
        notifications.forEach(item => {
            const li = document.createElement("li");

            // 알림 내용 텍스트
            const content = document.createElement("span");
            content.textContent = item.content;

            // createdAt 날짜를 한 줄 띄워서 회색으로 표시
            const createdAt = document.createElement("span");
            createdAt.textContent = item.createdAt; // 이 부분은 실제 `createdAt` 값을 넣어줘야 함
            createdAt.style.color = "gray";
            createdAt.classList.add("createdAt");
            createdAt.style.display = "inline-block"; // 한 줄 띄우기
            createdAt.style.fontSize = "0.9em"; // 작은 글씨 크기

            // 예약 상세 페이지 링크 추가
            const reservationLink = document.createElement("a");
            reservationLink.href = `/admin/reservation/details/${item.reservationPK}`;
            reservationLink.textContent = "예약 상세보기";
            reservationLink.style.display = "inline-block"; // 새 줄로 표시

            reservationLink.addEventListener("click", function (e) {
                e.preventDefault();

                fetch(`/api/notification/mark-read/${item.notificationPK}`, {
                    method: "POST"
                })
                    .then(() => {
                        window.location.href = reservationLink.href;
                    })
                    .catch(err => console.error("읽음 처리 실패", err));

            });

            // 링크와 알림 텍스트, 날짜를 리스트 항목에 추가
            li.appendChild(content);
            li.appendChild(createdAt);
            li.appendChild(reservationLink);

            // 리스트에 추가
            listItems.appendChild(li);
        });
    }

}
function connectWebSocket() {
    const socket = new SockJS('/ws'); // ✅ 백엔드에서 열어둔 엔드포인트 맞춰야 해
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log("📡 WebSocket 연결됨:", frame);

        stompClient.subscribe('/topic/admin', function (notification) {
            const body = JSON.parse(notification.body);

            appendNotification(body); // 통째로 전달
            increaseNotificationCount();

        });
    });
}

function appendNotification(notificationDto) {
    const listItems = document.getElementById("notification-items");

    const li = document.createElement("li");

    // 알림 텍스트
    const content = document.createElement("span");
    content.textContent = notificationDto.content;

    // 날짜
    const createdAt = document.createElement("span");
    createdAt.textContent = notificationDto.createdAt;
    createdAt.style.color = "gray";
    createdAt.classList.add("createdAt");
    createdAt.style.display = "inline-block";
    createdAt.style.fontSize = "0.9em";

    // 상세보기 링크
    const reservationLink = document.createElement("a");
    reservationLink.href = `/admin/reservation/details/${notificationDto.reservationPK}`;
    reservationLink.textContent = "예약 상세보기";
    reservationLink.style.display = "inline-block";

    reservationLink.addEventListener("click", function (e) {
        e.preventDefault();

        fetch(`/api/notification/mark-read/${notificationDto.notificationPK}`, {
            method: "POST"
        })
            .then(() => {
                window.location.href = reservationLink.href;
            })
            .catch(err => console.error("읽음 처리 실패", err));
    });

    li.appendChild(content);
    li.appendChild(createdAt);
    li.appendChild(reservationLink);

    listItems.insertBefore(li, listItems.firstChild);

    // 5개 이상이면 마지막 제거
    if (listItems.children.length > 5) {
        listItems.removeChild(listItems.lastElementChild);
    }
}

function increaseNotificationCount() {
    const badge = document.getElementById("notification-badge");

    let current = parseInt(badge.innerText) || 0;
    current++;

    badge.innerText = `${current}건`;
    badge.style.display = "block";
}

const reservationStateElements = document.querySelectorAll(".reservationState");


//예약에 따라 색깔 다르게 적용
reservationStateElements.forEach((checkoutStepElement, index) => {
    const checkoutStep = checkoutStepElement.textContent;

    if (checkoutStep == "예약완료") {
        checkoutStepElement.style.color = "blue";
    }
    else if (checkoutStep == "예약보류") {
        checkoutStepElement.style.color = "green";
    }
    else {
        checkoutStepElement.style.color = "red";
    }
});


