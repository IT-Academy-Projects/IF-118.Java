package com.softserve.itacademy.config;

import com.softserve.itacademy.repository.ChatRoomRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.AccessManager;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name="accessManager")
    public AccessManager accessManager(GroupRepository groupRepository, ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        return new AccessManager(groupRepository, chatRoomRepository, userRepository);
    }

    @Bean
    public ProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

}