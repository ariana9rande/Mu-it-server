package com.hjh.muit.config.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjh.muit.config.jwt.TokenProvider;
import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.repository.UserRepository;
import com.hjh.muit.service.RefreshTokenService;
import com.hjh.muit.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;
    private final UserService userService;

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

            String accessToken = tokenProvider.generateAccessToken(user);

            String refreshToken = tokenProvider.generateRefreshToken(user);
            refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

            userService.updateLastLogin(user.getId());

            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("userId", user.getLoginId());
            dataMap.put("accessToken", accessToken);

            log.info("userId = {}", user.getLoginId());
            log.info("accessToken = {}", accessToken);
            log.info("refreshToken = {}", refreshToken);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);

            response.getWriter().write(objectMapper.writeValueAsString(ApiResponseDto.success("oauth2 로그인 성공", dataMap)));
        }

        if (user == null) {
            //TODO 회원가입 처리
            log.info("회원가입 절차 필요");

            response.setContentType("application/json;charset=utf-8");

            response.getWriter().write(objectMapper.writeValueAsString(ApiResponseDto.error("회원가입 필요", HttpStatus.UNAUTHORIZED, email)));
        }

    }
}
