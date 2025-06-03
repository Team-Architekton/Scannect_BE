package com.example.scannect.service;

import com.example.scannect.dto.UserDTO;
import com.example.scannect.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    //회원가입
    public void register(UserDTO user) {
        userMapper.insert(user);
    }

    //아이디 중복 체크
    public boolean isDuplicateId(String id) {
        return userMapper.findById(id) != null;
    }
    //로그인
    public UserDTO login(String id, String password) {
        return userMapper.login(id, password);
    }


}
