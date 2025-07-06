package com.hjh.muit.config.oauth2;

import com.hjh.muit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(request);
        log.info("oauth2User : {}", oauth2User);

        String registrationId = request.getClientRegistration().getRegistrationId(); // google, kakao
        log.info("registrationId = {}", registrationId);

        Map<String, Object> attributes = oauth2User.getAttributes();
        log.info("attributes = {}", attributes);

        String userNameAttributeName = request
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("userNameAttributeName = {}", userNameAttributeName);

        return new CustomOAuth2User(oauth2User, registrationId);
    }
}
