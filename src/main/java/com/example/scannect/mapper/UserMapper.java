package com.example.scannect.mapper;

import com.example.scannect.dto.UserDTO;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    UserDTO findById(@Param("id") String id);

    void insert(UserDTO user);

    UserDTO login(@Param("id") String id, @Param("password") String password);

}
