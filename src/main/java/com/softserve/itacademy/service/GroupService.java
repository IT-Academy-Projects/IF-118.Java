package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.dto.GroupDto;
import com.softserve.itacademy.entity.dto.UserDto;

import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();
    Boolean delete(Integer id);
}
