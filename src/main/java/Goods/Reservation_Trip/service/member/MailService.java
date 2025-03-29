package Goods.Reservation_Trip.service.member;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    private final RedisTemplate<String,String> redisTemplate;

    public void setCode(String email,String authCode){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        //유효 시간은 300 , 단위는 초로 설정
        valueOperations.set(email,authCode,300, TimeUnit.SECONDS);
    }

   public String getCode(String email) throws AuthenticationException{
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String authCode = valueOperations.get(email);
        if(authCode == null)
            return "인증번호가 일치하지 않습니다.";
        return authCode;
   }

   public String sendSimpleMessage(String email){

       //인증번호 생성
       StringBuilder code = new StringBuilder();

       //0~9까지 랜덤 숫자 생성후 6자리로 만듦
       Random random = new Random() ;

       for(int i =0 ; i<6 ; i++){
           code.append((int)(random.nextInt(10)));
       }

       String content = "인증번호 : " + code;
       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo(email);
       message.setFrom(fromEmail);
       message.setSubject("[냥만여행]인증코드가 발송되었습니다.");
       message.setText(content);

       try {
           mailSender.send(message);
       }
       catch (Exception e){
           throw new RuntimeException("인증코드 전송을 실패했습니다.");
       }
       return code.toString();
   }

   public boolean sendAuthCode(String email) throws MessagingException{
        String authCode = sendSimpleMessage(email);
        setCode(email,authCode);
        return true;
   }
   
   public boolean validationAuthCode(String email, String authCode) throws AuthenticationException{
        String savedCode = getCode(email);
        return authCode.equals(savedCode); //인증번호가 일치하는지 확인
   }

   public String sendMailToChangePassword(String email){
       StringBuilder randomPassword = new StringBuilder();
       Random word = new Random();

       //8자리의 랜던 영문자 만듦
       for(int i=0;i<8;i++){
           randomPassword.append((char)(word.nextInt(26)+65));
       }

       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo(email);
       message.setFrom(fromEmail);
       message.setSubject("[냥만여행]임시비밀번호가 발송되었습니다.");
       message.setText("임시비밀번호 : "+ randomPassword.toString());

       try {
           mailSender.send(message);
       }
       catch (Exception e){
           throw new RuntimeException("인증코드 전송을 실패했습니다.");
       }
        
       //랜덤으로 만들어진 비밀번호 반환
       return randomPassword.toString();

   }


}
