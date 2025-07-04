package com.hjh.muit.config.oauth2;

import com.hjh.muit.entity.User;
import com.hjh.muit.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//        log.info("principal = {}", authentication.getPrincipal());

        String email = oauthUser.getAttribute("email");
//        log.info("email = {}", email);
        User user = userRepository.findByEmail(email).orElse(null);
//        log.info("user = {}", user);

        if (user != null) {
            if (!user.getProvider().equals(oauthUser.getProvider())) {
                log.error("다른 방식으로 이미 가입된 이메일입니다.");
                getRedirectStrategy().sendRedirect(request, response, "/login");
                return;
            }

            user.setLastLogin(LocalDateTime.now());
        }

        if (user == null) {
            getRedirectStrategy().sendRedirect(request, response, "/signup");
            return;
        }

        // 정상 로그인 후 이동
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
