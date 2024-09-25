package com.restapi.project_AI_diary_backend.domain.user.controller;


import com.restapi.project_AI_diary_backend.common.api.Api;
import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.domain.token.controller.model.TokenResponse;
import com.restapi.project_AI_diary_backend.domain.user.business.UserBusiness;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserDto;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserLoginRequest;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserRegisterRequest;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserResponse;
import com.restapi.project_AI_diary_backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins="http://localhost:3000", allowedHeaders="*")
public class UserOpenApiController {

    private final UserService userService;
    private final UserBusiness userBusiness;

    @PostMapping("/register")
    public ResponseEntity<Api<UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request
    ) {
        var response = userBusiness.register(request);  // response가 UserResponse 타입이어야 함
        return ResponseEntity.ok(Api.OK(response));
    }


    @PostMapping("/login")
    public ResponseEntity<Api<TokenResponse>> login(
            @Valid @RequestBody UserLoginRequest request
    ){
        var response = userBusiness.login(request);
        return ResponseEntity.ok(Api.OK(response));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Api<UserDto>> getUserByUsername(@PathVariable String username) { // UserDto 사용
        UserDto userDto = userService.searchByUsername(username); // UserDto 반환
        if (userDto == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다.");
        }
        return ResponseEntity.ok(Api.OK(userDto));
    }
}
