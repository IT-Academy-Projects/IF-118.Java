package com.softserve.itacademy.it;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class BasicTest {

    private HttpClient httpClient = HttpClientBuilder.create().build();
}
