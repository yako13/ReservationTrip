package Goods.Reservation_Trip.dto.member.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditDto {

    //이메일
    @NotBlank(message = "이메일은 필수 정보입니다.")
    private String email;

    //인증번호
    @NotBlank(message = "인증번호는 필수 정보입니다.")
    private String authCode;

    //비밀번호
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$"
            ,message = "비밀번호는 영대소문자, 숫자, 특수문자조합으로 8~15자리여야합니다.")
    private String password;

    //이름
    @Pattern(regexp = "^[가-힣]{2,5}$|^[a-zA-Z]{1,10}\\s[a-zA-Z]{1,10}$" , message = "이름은 한글 2~5자 또는 성과 이름을 구분하여 영문 2~10자리씩이여야 합니다.")
    @NotBlank
    private String name;

    //생년월일
    @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",message = "생년월일은 8자리의 숫자여야합니다.")
    @NotBlank
    private String birth;

    //휴대전화번호
    @Pattern(regexp = "^(010|011|016|017|018|019)[0-9]{7,8}$",message = "휴대전화번호는 010, 011, 016, 017, 018, 019로 시작하며, 숫자 10~11자리여야 합니다.")
    @NotBlank
    private String phoneNumber;

    //성별
    private boolean gender;
}
