package com.restapi.project_AI_diary_backend.common.interceptor;


import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.error.TokenErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.db.user.UserRepository;
import com.restapi.project_AI_diary_backend.domain.token.business.TokenBusiness;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;
    private final UserRepository userRepository; // UserRepository 주입

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // WEB, chrome의 경우 GET, POST OPTIONS
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // js, html, png resource를 요청하는 경우 = pass
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // 헤더 토큰 검증
        var accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        var email = tokenBusiness.validationAccessToken(accessToken);

        if (email != null) {
            var userOptional = userRepository.findByEmail(email); // email로 사용자 조회

            if (userOptional.isPresent()) {
                var username = userOptional.get().getUsername(); // username 가져오기
                var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
                requestContext.setAttribute("username", username, RequestAttributes.SCOPE_REQUEST); // username 저장
                return true;
            } else {
                throw new ApiException(ErrorCode.BAD_REQUEST, "사용자를 찾을 수 없습니다.");
            }
        }

        throw new ApiException(ErrorCode.BAD_REQUEST, "인증실패");
    }
}
