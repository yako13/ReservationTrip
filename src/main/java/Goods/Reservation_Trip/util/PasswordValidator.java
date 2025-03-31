package Goods.Reservation_Trip.util;

import Goods.Reservation_Trip.inter.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword,String> {

    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        //null허용
        if(s == null || s.isEmpty()){
            return true;
        }

        //값이 입력되었을 경우 정규식 검증
        return s.matches(PASSWORD_REGEX);
    }
}
