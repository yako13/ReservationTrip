//창 실행시 알람창 호출
window.addEventListener('load', dataAlertFunction());

//플래시 어트리뷰트로 가져온 값을 알람창으로 보여주는 함수
function dataAlertFunction() {
  let alertElement = document.querySelector("#dataAlert");
  let alertElement2 = document.querySelector("#dataAlert2");

  if (alertElement && alertElement.value.trim() !== "") { // 데이터가 존재하고 빈값 ""이 아닐경우 실행
    let message = alertElement.value;

    alert(message); // 기본 알람창 띄우기

    // FlashAttribute 값이 URL에 남지 않도록 초기화
    history.replaceState({}, document.title, window.location.pathname);

  }

  if (alertElement2 && alertElement2.value.trim() !== "") { // 데이터가 존재하고 빈값 ""이 아닐경우 실행
      let message = alertElement2.value;

      alert(message); // 기본 알람창 띄우기

      // FlashAttribute 값이 URL에 남지 않도록 초기화
      history.replaceState({}, document.title, window.location.pathname);

    }
};

// 회원가입 완료 페이지 (초기화)
window.onload = function() {
    sessionStorage.removeItem("agreedToTerms");
};