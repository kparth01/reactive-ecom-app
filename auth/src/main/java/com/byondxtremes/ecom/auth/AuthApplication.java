package com.byondxtremes.ecom.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.byondxtremes.ecom.auth"})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        //BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        //System.out.println(b.encode("javainuse"));
    }

} 
