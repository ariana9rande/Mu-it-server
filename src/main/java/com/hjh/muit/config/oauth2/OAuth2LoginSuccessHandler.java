package com.hjh.muit.config.oauth2;

import com.hjh.muit.config.jwt.TokenProvider;
import com.hjh.muit.entity.User;
import com.hjh.muit.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    public static final String REDIRECT_PATH = "/";

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate redisTemplate;

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

            String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
            saveRefreshToken(user.getId(), refreshToken);
            addRefreshTokenToCookie(request, response, refreshToken);

            String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
            String targetUrl = getTargetUrl(accessToken);

            clearAuthenticationAttributes(request, response);

            user.setLastLogin(LocalDateTime.now());
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }

        if (user == null) {
            //TODO 회원가입 처리
            log.info("회원가입 절차 필요");
//            getRedirectStrategy().sendRedirect(request, response, "/signup");
            return;
        }

        // 정상 로그인 후 이동
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
