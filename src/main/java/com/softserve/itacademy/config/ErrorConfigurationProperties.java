package com.softserve.itacademy.config;

import com.softserve.itacademy.request.ErrorRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "errors")
public class ErrorConfigurationProperties {
    Map<String, ErrorRequest> exceptions;
}
