package com.example.service;

import com.example.dto.BaseResponse;
import com.example.model.ResponceDTO.SignInDTO;
import com.example.model.CreateDTO.UserCreateDTO;
import com.example.model.ResponceDTO.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public String createUser(UserCreateDTO dto) {
        HttpEntity<UserCreateDTO> userDTO= new  HttpEntity<>(dto);
        ResponseEntity<BaseResponse<UserResponseDTO>> exchange = restTemplate.exchange(
                backendHost + "/user/add",
                HttpMethod.POST,
                userDTO,
                new ParameterizedTypeReference<BaseResponse<UserResponseDTO>>() {}
        );
        System.out.println("exchange.getBody() = " + exchange.getBody().getData());

        return "success";
    }

    public UserResponseDTO signIn(SignInDTO dto) {
        HttpEntity<SignInDTO> dtoHttpEntity = new HttpEntity<>(dto);

//        ResponseEntity<BaseResponse<UserResponseDTO>> entity = restTemplate.postForEntity(
//                backendHost + "/user/sign-in",
//                dtoHttpEntity,
//                new ParameterizedTypeReference<BaseResponse<UserResponseDTO>>() {}
//        );
        /**ResponseEntity<BaseResponse<UserResponseDTO>> exchange = restTemplate.exchange(
         backendHost + "/user/sign-in",
         HttpMethod.POST,
         dtoHttpEntity,
         new ParameterizedTypeReference<BaseResponse<UserResponseDTO>>() {}
         );*/

        ResponseEntity<BaseResponse<UserResponseDTO>> exchange = restTemplate.exchange(
                backendHost + "/user/sign-in",
                HttpMethod.POST,
                dtoHttpEntity,
                new ParameterizedTypeReference<BaseResponse<UserResponseDTO>>() {}
        );
        return (UserResponseDTO) Objects.requireNonNull(exchange.getBody()).getData();
    }

    public UserResponseDTO getById(UUID Id) {
        HttpEntity<UUID> userId =  new HttpEntity<>(Id);
        ResponseEntity<BaseResponse<UserResponseDTO>> exchange = restTemplate.exchange(
                backendHost + "/user/get-by-id",
                HttpMethod.POST,
                userId,
                new ParameterizedTypeReference<BaseResponse<UserResponseDTO>>() {}
        );
        return (UserResponseDTO) exchange.getBody().getData();
    }
}
