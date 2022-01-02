package com.sp.fc.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl() {
        return "http://localhost:" + port + "/greeting";
    }

    @DisplayName("1. 인증 실패")
    @Test
    void test_1(){

        // 막아놨으므로 clinet error 발생
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            String response = client.getForObject(greetingUrl(), String.class);
        });

        // 기댓값 401 error
        assertEquals(401, exception.getRawStatusCode());

    }

    @DisplayName("2. 인증 성공")
    @Test
    void test_2(){

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<String> res = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);// get 방식으로 entity를 가지고 와서 String으로 data 가져 옴

        assertEquals("hello", res.getBody());
    }

    @DisplayName("3. 인증 성공 2")
    @Test
    void test_3(){
        // TestRestTemplate은 바로 basicHeaderToken을 넣어서 request를 날려 줌
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String res = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", res);
    }

    @DisplayName("4. POST 인증")
    @Test
    void test_4(){
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> res  = testClient.postForEntity(greetingUrl(), "jeongyeon", String.class);
        assertEquals("hello jeongyeon", res.getBody());
    }

}
