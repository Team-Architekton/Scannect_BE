package com.example.scannect.controller;

import com.example.scannect.common.ApiResponse;
import com.example.scannect.common.ResponseService;
import com.example.scannect.dto.UserDTO;
import com.example.scannect.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    public UserController(UserService userService, ResponseService responseService) {
        this.userService = userService;
        this.responseService = responseService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerUser(@RequestBody UserDTO user) {

        if (userService.isDuplicateId(user.getId())) {
            return ResponseEntity.ok(responseService.fail("이미 존재하는 아이디입니다."));
        }

        userService.register(user);
        return ResponseEntity.ok(responseService.successMessage("회원가입 완료"));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userRequest) {
        UserDTO user = userService.login(userRequest.getId(), userRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
