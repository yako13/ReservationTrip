document.addEventListener('DOMContentLoaded', function () {
  const msgInput = document.getElementById('flashMessage');
  const message = msgInput?.value;
  const msgInput2 = document.getElementById('flashMessage2');
  const message2 = msgInput2?.value;
    const msgInput3 = document.getElementById('flashMessage3');
    const message3 = msgInput3?.value;

  if (message) {
      Swal.fire({
          icon: 'error',
          title: '오류 발생',
          text: message,
          confirmButtonText: '확인'
      });
  }

  if (message2) {
    Swal.fire({
        text: message2,
        confirmButtonText: '확인'
    });
}


 if (message3) {
    Swal.fire({
        icon: 'info',
        title: '안내',
        text: message3,
        confirmButtonText: '확인'
    });
  }

});

// 회원가입 완료 페이지 (초기화)
window.onload = function() {
    sessionStorage.removeItem("agreedToTerms");
};