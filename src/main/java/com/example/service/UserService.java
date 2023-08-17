package com.example.service;

import com.example.model.SignInDTO;
import com.example.model.UserCreateDTO;
import com.example.model.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public String createUser(UserCreateDTO dto) {
        HttpEntity<UserCreateDTO> userDTO= new  HttpEntity<>(dto);
        ResponseEntity<UserResponseDTO> exchange = restTemplate.exchange(
                backendHost + "/user/add",
                HttpMethod.POST,
                userDTO,
                UserResponseDTO.class

        );
        System.out.println("exchange.getBody() = " + exchange.getBody());

        return "success";
    }

    public UserResponseDTO signIn(SignInDTO dto) {
        HttpEntity<SignInDTO> dtoHttpEntity = new HttpEntity<>(dto);
        ResponseEntity<UserResponseDTO> exchange = restTemplate.exchange(
                backendHost + "/user/sign-in",
                HttpMethod.POST,
                dtoHttpEntity,
                UserResponseDTO.class
        );
        return exchange.getBody();
    }
}
