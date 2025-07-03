package com.hjh.muit.service;

import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.OAuthUserDto;
import com.hjh.muit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);

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

        OAuthUserDto dto = OAuthUserDto.of(registrationId, userNameAttributeName, attributes);
        log.info("dto = {}", dto);

        User user = processOAuthUser(dto);

        // 필수: OAuth2User 리턴
        return oauth2User;
    }

    public User processOAuthUser(OAuthUserDto dto) {
        Optional<User> optionalExistingUser = userRepository.findByEmail(dto.getEmail());

        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            if (!existingUser.getProvider().equals(dto.getProvider())) {
                throw new OAuth2AuthenticationException("다른 방식으로 이미 가입된 이메일입니다.");
            }

            existingUser.setLastLogin(LocalDateTime.now());
            return existingUser;
        }

        return null;
    }
}
