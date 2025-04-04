package Goods.Reservation_Trip.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Formatter {



    public static String changeBigDecimalFormat(BigDecimal bigDecimal){
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(bigDecimal) + " 원";
    }


    public static String getLocalDate(LocalDateTime localDateTime){
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



    public static String changePhoneNumber(String phoneNumber){
        if(phoneNumber.length()==11){
            return phoneNumber.substring(0,3)+"-"+phoneNumber.substring(3,7)+"-"+phoneNumber.substring(7,11);
        }
        else if(phoneNumber.length()==10){
            return phoneNumber.substring(0,3)+"-"+phoneNumber.substring(3,6)+"-"+phoneNumber.substring(6,10);
        }
        throw new RuntimeException("잘못된 전화번호");
    }

    public static String changeCardNumber(String cardNumber){
        return cardNumber.substring(0,4)+"-"+cardNumber.substring(4,8)+"-****-****";
    }



    //주문번호 숫자에 붙여주는 1~1000자리 숫자
    public class IncrementalCounter {
        private static int counter = 0; // 클래스 변수로 유지

        public static String getNextNumber() {
            counter = (counter % 1000) + 1; // 1~1000까지 순환
            return String.format("%04d", counter);
        }
    }

    //주문 번호 만드는 함수
    public static String getReservationCode(LocalDateTime localDateTime) {
        if(localDateTime !=null) {
            String dateTimePart = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String counterPart = IncrementalCounter.getNextNumber(); // 3자리 숫자 추가
            return dateTimePart + counterPart;
        }
        return null;
    }

    //패키지명에서 태그 추출
    public static String getTag(String packageName){
        return packageName.substring(packageName.indexOf("#"));
    }

    //패키지명에서 태그만 뺀 값
    public static String getPackageNameWithoutTag(String packageName){
        return packageName.substring(0,packageName.indexOf("#"));
    }


}
