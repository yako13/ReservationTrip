package Goods.Reservation_Trip.dto.member.req;



import Goods.Reservation_Trip.inter.OAuth2UserInfo;

import java.util.Map;

public class KakaoUserDetails implements OAuth2UserInfo {
    private Map<String,Object> attributes;

    public KakaoUserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public String getGender(){ return null;}

    @Override
    public String getBirth(){
        return null;
    }
}
