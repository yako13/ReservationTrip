package Goods.Reservation_Trip.util;

import Goods.Reservation_Trip.entity.PackageOption;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Formatter {

    //패키지 옵션
    public static List<String> getPackageOption(PackageOption packageOption) {
        List<String> optionList = new ArrayList<>();
        if (packageOption.isHotelFee()) {
            optionList.add("호텔비 포함");
        } else {
            optionList.add("호텔비 미포함");
        }
        if (packageOption.isGuide()) {
            optionList.add("가이드 있음");
        } else {
            optionList.add("가이드 없음");
        }
        if (packageOption.isAirfare()) {
            optionList.add("항공료 포함");
        } else {
            optionList.add("항공료 미포함");
        }
        if (packageOption.isNoShopping()) {
            optionList.add("쇼핑 없음");
        } else {
            optionList.add("쇼핑 필수");
        }
        return optionList;
    }


    public static String changeBigDecimalFormat(BigDecimal bigDecimal) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(bigDecimal) + " 원";
    }


    public static String getLocalDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    public static String changePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        }
        if(phoneNumber.equals("")){
            return "";
        }
        if (phoneNumber.length() == 11) {
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7, 11);
        } else if (phoneNumber.length() == 10) {
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6, 10);
        } else {
            throw new RuntimeException("잘못된 전화번호");
        }
    }


//--Han Part 시작--


    //초가 같을시 숫자 증가시켜주는 함수
    private static class IncrementalCounter {
        private static int counter = 0;
        private static LocalDateTime lastDateTime = null;

        public static String getNextNumber(LocalDateTime currentDateTime) {
            if (lastDateTime != null && currentDateTime.equals(lastDateTime)) {
                // 같은 시간이면 카운터 증가
                counter = (counter % 1000) + 1;
            }
            // 다른 시간이면 counter 유지
            else if (lastDateTime == null) {
                // 첫 호출은 counter 초기화
                counter = 1;
            }

            lastDateTime = currentDateTime; // 갱신은 무조건 수행
            return String.format("%04d", counter);
        }
    }

    // 주문 번호 생성 함수
    public static String getReservationCode(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            String dateTimePart = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String counterPart = IncrementalCounter.getNextNumber(localDateTime);
            return dateTimePart + counterPart;
        }
        return null;
    }

    //패키지명에서 태그 추출
    public static String getTag(String packageName) {
        if (packageName.contains("#")) {
            return packageName.substring(packageName.indexOf("#"));
        }
        return null;
    }

    //패키지명에서 태그만 뺀 값
    public static String getPackageNameWithoutTag(String packageName) {
        if (packageName.contains("#")) {
            return packageName.substring(0, packageName.indexOf("#"));
        }
        return packageName;
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

    //생년월일 분류
    public static String getBirth(String birth) {
        return birth.substring(0, 4) + "." + birth.substring(4, 6) + "." + birth.substring(6);
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

    // 25.05.01(목) 09:35 형식으로 포매팅
    public static String formatDayAndTime(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("date 또는 time이 null입니다");
        }

        String formattedDate = date.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

        return String.format("%s(%s) %s", formattedDate, dayOfWeek, formattedTime);
    }

    //25.01.01 식으로 생년월일을 변환해주는 함수
    public static String formatBirthDate(String birthDate) {
        if (birthDate == null || birthDate.length() != 8) {
            throw new IllegalArgumentException("생년월일은 8자리여야 합니다 (예: 20150101)");
        }

        String year = birthDate.substring(0, 4);
        String month = birthDate.substring(4, 6);
        String day = birthDate.substring(6, 8);

        return String.format("%s.%s.%s", year, month, day);
    }

    //BigDecimal을 원단위와 원을 붙여주는 함수 그리고 원 앞에 뛰어쓰기 없는 버전
    public static String BigDecimalFormat(BigDecimal bigDecimal) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(bigDecimal) + "원";
    }

    //BigDecimal을 원단위와 원을 붙여주는 함수 그리고 원이 없음
    public static String BigDecimalFormat2(BigDecimal bigDecimal) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(bigDecimal) ;
    }

    //몇박 몇일 계산 해주는 함수
    public static String TripDuration(LocalDate startDate, LocalDate endDate) {

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1; // 며칠

        long nights = days - 1; // 몇 박

        //몇박 몇일로 출력
        String tripDuration = nights + "박 " + days + "일";

        return tripDuration;
    }

    //일수를 몇박 몇일로 바꿔 주는 함수
    public static String TripDate(int date) {

        // 몇 박
        long nights = date - 1;

        //몇박 몇일로 출력
        String tripDuration = nights + "박 " + date + "일";

        return tripDuration;
    }






    //--Han Part2 끝--


}
