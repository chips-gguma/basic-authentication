package com.sp.fc.web.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()   // 사용자 생성
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                                .build()
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // POST 방식은 csrf filter 작동하므로 disable 시켜주어야 POST 인증이 가능하다.
                // SPA 페이지에서 RestController의 서비스를 하려고 할 때 위와 같이 해주어야 하는데
                // 서버가 동시에 웹페이지도 서비스를 해서 csrf를 enable() 시켜야 한다면 어떻게 해야 할까?
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated() // 모든 request에 대해 authenticated()로 막음
                .and()
                .httpBasic()    // 그리고 httpBasic()으로 들어와
                ;
    }
}
