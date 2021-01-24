package com.softserve.itacademy.config;

import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.security.Decider;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name="decider")
    public Decider deciderService(GroupRepository groupRepository) {
        return new Decider(groupRepository);
    }
}