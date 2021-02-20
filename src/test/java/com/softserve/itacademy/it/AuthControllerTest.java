package com.softserve.itacademy.it;

import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.tools.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("classpath:db/insert_roles.sql")
public class AuthControllerTest extends BasicTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testRegisterUser() throws Exception {
        RegistrationRequest dto = RegistrationRequest.builder()
                .email("test@test.com")
                .name("John")
                .password("password1")
                .pickedRole("TEACHER")
                .build();

        HttpPost request = new HttpPost("http://localhost:8888/api/v1/registration");
        String json = HttpUtil.asJsonString(dto);
        StringEntity entity = new StringEntity(json);
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        HttpResponse response = getHttpClient().execute(request);

        SuccessRegistrationResponse resource = HttpUtil.retrieveResourceFromResponse(response, SuccessRegistrationResponse.class);

        Integer count = JdbcTestUtils.countRowsInTable(new JdbcTemplate(dataSource), "users");
        assertEquals("test@test.com", resource.getEmail());
        assertEquals(1, count);
    }
}
