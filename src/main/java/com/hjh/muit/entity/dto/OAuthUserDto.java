package com.hjh.muit.entity.dto;

import com.hjh.muit.enums.UserRole;
import com.hjh.muit.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthUserDto {

    private String email;
    private String provider;
    private String providerId;
    private String nameAttributeKey;
    private Map<String, Object> attributes;

    public static OAuthUserDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthUserDto ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthUserDto.builder()
                .email((String) attributes.get("email"))
                .provider("google")
                .providerId((String) attributes.get(userNameAttributeName))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuthUserDto ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuthUserDto.builder()
                .email((String) kakaoAccount.get("email"))
                .provider("kakao")
                .providerId(String.valueOf(attributes.get(userNameAttributeName)))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .providerId(providerId)
                .provider(provider)
                .role(UserRole.USER)
                .build();
    }
}
