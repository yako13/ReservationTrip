package Goods.Reservation_Trip.inter;


public interface OAuth2UserInfo {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    String getPhoneNumber();

    String getBirth();

    String getGender();
}


