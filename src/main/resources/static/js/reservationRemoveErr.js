
document.addEventListener('DOMContentLoaded', () => {
 

  //입력시 에러메세지 숨기고 빨간줄 제거
  document.querySelectorAll('.HanNameReq').forEach(input => {
    input.addEventListener('input', () => {
      const wrapper = input.closest('.HanNameDiv');
      input.classList.remove('HanRed');
      if (wrapper) wrapper.classList.remove('HanRedName');


    });
  });

  document.querySelectorAll('.HanBirthReq').forEach(input => {
    input.addEventListener('input', () => {
      input.classList.remove('HanRed');


    });
  });

  document.querySelectorAll('.HanPhoneReq').forEach(input => {
    input.addEventListener('input', () => {
      input.classList.remove('HanRed');


    });
  });

  //성별 라디오 체크시 빨간줄 및 에러메세지 제거
  document.querySelectorAll('input[type="radio"][name*=".gender"]').forEach(radio => {
    radio.addEventListener('change', () => {
      const wrapper = radio.closest('.HanGenderRadio');
      const parent = wrapper?.closest('.HanNameDiv');
      const msg = parent?.parentElement?.querySelector('.HanRedPGender');

      if (wrapper) wrapper.classList.remove('HanRedGender');

    });
  });

});
