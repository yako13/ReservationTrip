window.onscroll = function () {

  // 버튼 요소 가져오기
  const scrollToTopBtn = document.getElementById('scrollToTopBtn');

  if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 100) {
    scrollToTopBtn.style.display = "block";
  } else {
    scrollToTopBtn.style.display = "none";
  }
};

// 버튼 클릭 시 맨 위로 스크롤
scrollToTopBtn.onclick = function () {
  window.scrollTo({ top: 0, behavior: 'smooth' });
};
