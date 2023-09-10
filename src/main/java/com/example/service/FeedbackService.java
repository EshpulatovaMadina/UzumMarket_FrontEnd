package com.example.service;

import com.example.dto.BaseResponse;
import com.example.model.CreateDTO.BasketCreateDTO;
import com.example.model.CreateDTO.FeedbackCreateDTO;
import com.example.model.ResponceDTO.BasketResponseDTO;
import com.example.model.ResponceDTO.FeedbackResponseDTO;
import com.example.model.ResponceDTO.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;
    public String create( UUID productId, UUID userId, int rate ,String text){
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO(productId, userId, rate, text);

        HttpEntity<FeedbackCreateDTO> dtoHttpEntity = new HttpEntity<>(feedbackCreateDTO);
        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/feedback/create",
                HttpMethod.POST,
                dtoHttpEntity,
                BaseResponse.class
        );
        if (Objects.requireNonNull(exchange.getBody()).getData() != null) {
            return "saved";
        }
        return "wrong";
    }


    public FeedbackResponseDTO getById(UUID feedbackId) {
        ResponseEntity<BaseResponse<FeedbackResponseDTO>> exchange = restTemplate.exchange(
                backendHost + "/feedback/getById/"+feedbackId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<FeedbackResponseDTO>>() {});

                new ParameterizedTypeReference<BaseResponse<FeedbackResponseDTO>>() {};
        return exchange.getBody().getData();
    }

    public void  delete(UUID feedbackId){
        restTemplate.exchange(backendHost + "/feedback/delete/"+feedbackId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<BaseResponse<FeedbackResponseDTO>>(){});
    }

    public List<FeedbackResponseDTO> feedbackOfProduct(UUID productId){
        ResponseEntity<BaseResponse<List<FeedbackResponseDTO>>> exchange = restTemplate.exchange(backendHost + "/feedback/feedbackOfProduct" + productId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<List<FeedbackResponseDTO>>>() {
                });
        return exchange.getBody().getData();
    }
}
