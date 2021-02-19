package com.softserve.itacademy.it;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
@ActiveProfiles("test")
public class BasicTest {

    protected HttpClient httpClient = HttpClientBuilder.create().build();
}
