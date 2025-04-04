package Goods.Reservation_Trip.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
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




    //--Han Part 시작--


    //카드 번호 포멧 (뒷자리 8자리 * / 4자리마다 - 붙여주기)
    public static String CardNumFormat(String CardNum) {

        if (CardNum == null || CardNum.length() != 16) {
            return "Invalid Card Number";
        }

        // 앞 8자리 유지, 뒷 8자리는 *로 마스킹
        String masked = CardNum.substring(0, 8) + "********";

        // 4자리마다 '-' 추가
        String cardNumFormatString = masked.replaceAll("(.{4})", "$1-").substring(0, 19);

        return cardNumFormatString;
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

    //운송장 번호 만드는 함수
    public static String generateDeliveryCode() {

        Random random = new Random();

        // 4자리씩 3개의 랜덤 숫자 생성
        int part1 = random.nextInt(10000); // 0000 ~ 9999
        int part2 = random.nextInt(10000);
        int part3 = random.nextInt(10000);

        // 각 숫자를 4자리로 맞추기 위해 String.format 사용
        return String.format("%04d-%04d-%04d", part1, part2, part3);
    }


    //-----HanPart 끝------

    //--Han Part2 시작--
    //25.01.01(화) 방식으로 포메팅 하는것
    public static String formatDateAndDay(LocalDate localDate) {
        if (localDate == null) {

            throw new IllegalArgumentException("localDate가 null입니다");
        }

        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        String day = localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN); // 일, 월, ...

        String result = formattedDate + "(" + day + ")";


        return result;
    }
    //25.01.01(화) 22:00 방식으로 포메팅 하는것
    public static String formatDateTimeWithDay(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new IllegalArgumentException("localDateTime 이 null입니다");
        }

        // 날짜 형식 포맷
        String formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        // 요일 추출 (예: 월, 화, 수...)
        String dayOfWeek = localDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        // 시간 형식 포맷
        String time = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        // 최종 문자열 조합
        return String.format("%s(%s) %s", formattedDate, dayOfWeek, time);
    }


    public static String formatBirthDate(String birthDate) {
        if (birthDate == null || birthDate.length() != 8) {
            throw new IllegalArgumentException("생년월일은 8자리여야 합니다 (예: 20150101)");
        }

        String year = birthDate.substring(0, 4);
        String month = birthDate.substring(4, 6);
        String day = birthDate.substring(6, 8);

        return String.format("%s.%s.%s", year, month, day);
    }

    public static String BigDecimalFormat(BigDecimal bigDecimal) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(bigDecimal) + "원";
    }


    //--Han Part2 끝--


}
