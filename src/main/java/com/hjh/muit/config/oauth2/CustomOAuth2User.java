package com.hjh.muit.config.oauth2;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@ToString
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    private final String provider;

    public CustomOAuth2User(OAuth2User oAuth2User, String provider) {
        this.oAuth2User = oAuth2User;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }
}
