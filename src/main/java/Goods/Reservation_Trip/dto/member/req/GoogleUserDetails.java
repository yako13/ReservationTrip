package Goods.Reservation_Trip.dto.member.req;

import Goods.Reservation_Trip.inter.OAuth2UserInfo;
import Spring.Goods_Shop.inter.OAuth2UserInfo;

import java.util.Map;

public class GoogleUserDetails implements OAuth2UserInfo {
    private Map<String,Object> attributes;

    public GoogleUserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }
}

