window.addEventListener('DOMContentLoaded', (event) => {
    openNav();

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


function delay() {
    document.querySelector(".SlideNavOpenDiv").classList.add("SlideShow");
}

const notificationList = document.getElementById("notification-list");

document.addEventListener("DOMContentLoaded", function () {
    // 알림 개수 및 목록을 LocalStorage에서 불러오기
    let notifications = JSON.parse(localStorage.getItem("notifications")) || [];
    let notificationCount = parseInt(localStorage.getItem("notificationCount")) || 0;

    notifications = notifications.map(item => {
        if (typeof item === "string") {
            return {
                message: item,
                time: new Date().toLocaleString()
            };
        }
        return item;
    });

    // WebSocket 연결
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/admin', function (notification) {
            const message = JSON.parse(notification.body);
            showNotification(message.message);
            refreshReservationList(); // 예약 리스트 새로고침
        });
    });

    // 알림 표시 함수
    function showNotification(msg) {
        const now = new Date();
        const formattedTime = now.toLocaleString(); // 실제 도착한 시각

        const newNotification = {
            message: msg,
            time: formattedTime
        };

        notifications.unshift(newNotification);
        if (notifications.length > 5) {
            notifications.pop();
        }

        notificationCount++;
        localStorage.setItem("notifications", JSON.stringify(notifications));
        localStorage.setItem("notificationCount", notificationCount);

        updateNotificationBadge();
        updateNotificationList();
    }

    // 알림 개수 UI 업데이트
    function updateNotificationBadge() {
        const notificationBadge = document.getElementById("notification-badge");
        if (notificationCount > 0) {
            notificationBadge.innerText = notificationCount + "건의 예약취소요청이 있습니다.";
            notificationBadge.style.display = "block";
        } else {
            notificationBadge.style.display = "none";
        }
    }

    // 알림 목록 UI 업데이트
    function updateNotificationList() {
        const notificationList = document.getElementById("notification-items");
        notificationList.innerHTML = "";
    
        if (notifications.length === 0) {
            notificationList.style.display = "none";
            return;
        }
    
        notificationList.style.display = "block";

        notifications.forEach((item) => {
            const listItem = document.createElement("li");
            const date = document.createElement("span");

            listItem.innerText = item.message;
            date.innerText = " (" + item.time + ")";
            date.style.fontSize = "12px";
            date.style.color = "gray";

            listItem.appendChild(date);
            notificationList.appendChild(listItem);
        });
    }

    // 배지 클릭 시 알림 목록 보이기/숨기기 + 개수 초기화
    document.getElementById("notification-badge").addEventListener("click", function () {
        if (notificationList.classList.contains("hidden")) {
            notificationList.classList.remove("hidden");
            notificationList.style.display = "block";

            // 개수 초기화 & LocalStorage 반영
            notificationCount = 0;
            localStorage.setItem("notificationCount", notificationCount);
            updateNotificationBadge();
        } else {
            notificationList.classList.add("hidden");
            notificationList.style.display = "none";
        }
    });

    // 초기화 버튼 클릭 시 알림 목록과 개수 초기화
    document.getElementById("clear-notifications").addEventListener("click", function () {
        // 알림 목록 초기화
        localStorage.removeItem("notifications");
        localStorage.setItem("notificationCount", 0);

        // UI 업데이트
        notifications = [];
        notificationCount = 0;
        updateNotificationBadge();
        updateNotificationList();
    });

    // UI 초기화 함수들 호출
    updateNotificationBadge();
    updateNotificationList();
});



let closeIcon = document.getElementById("closeIcon");
closeIcon.addEventListener("click", function () {
    notificationList.classList.add("hidden");
    notificationList.style.display = "none";
})

let bell = document.getElementById("bell");
bell.addEventListener("click", function () {
    notificationList.classList.remove("hidden");
    notificationList.style.display = "block";
})
