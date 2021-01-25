package com.softserve.itacademy.config;

import com.softserve.itacademy.repository.ChatRoomRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.security.AccessManager;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableScheduling
@EnableAsync
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name="accessManager")
    public AccessManager accessManager(GroupRepository groupRepository, ChatRoomRepository chatRoomRepository) {
        return new AccessManager(groupRepository, chatRoomRepository);
    }
}