package upcafe.security.model;

import upcafe.security.exception.OAuth2ProviderNotSupportedYetException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.compareToIgnoreCase("google") == 0) {
            return new GoogleUserInfo(attributes);
        } else {
            throw new OAuth2ProviderNotSupportedYetException(registrationId);
        }
    }
}
