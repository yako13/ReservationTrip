
        function validateKoreanNameInput(input) {
            const value = input.value.trim();
            const wrapper = input.closest('.HanNameDiv');
            const nameCheckP = wrapper?.parentElement?.querySelector('.HanRedPNameCheck');

            const isValid = /^[가-힣]+$/.test(value);

            if (!isValid) {
                wrapper?.classList.add('HanRedName');
                input.classList.add('HanRed');
                if (nameCheckP) nameCheckP.style.display = 'block';
            } else {
                wrapper?.classList.remove('HanRedName');
                input.classList.remove('HanRed');
                if (nameCheckP) nameCheckP.style.display = 'none';
            }
        }

        document.addEventListener('DOMContentLoaded', function () {
            const nameInputs = document.querySelectorAll('.HanNameReq');

            nameInputs.forEach(input => {
                input.addEventListener('input', () => validateKoreanNameInput(input));
            });
        });