package com.softserve.itacademy.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;



public class AuthControllerTest extends BasicTest {

    @Test
    void testRegisterUser() throws Exception {
//        httpClient.execute("api/v1/register");
    }

    @Test
    void testRegisterUser2() throws Exception {

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
