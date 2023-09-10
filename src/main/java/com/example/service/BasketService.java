package com.example.service;

import com.example.dto.BaseResponse;
import com.example.model.CreateDTO.BasketCreateDTO;
import com.example.model.CreateDTO.CategoryCreateDTO;
import com.example.model.ResponceDTO.BasketResponseDTO;
import com.example.model.ResponceDTO.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public String create(BasketCreateDTO dto){
        BasketCreateDTO basketCreateDTO = new BasketCreateDTO(dto.getUserId(), dto.getProductId(), dto.getCount());

        HttpEntity<BasketCreateDTO> dtoHttpEntity = new HttpEntity<>(basketCreateDTO);

        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/basketController/add",
                HttpMethod.POST,
                dtoHttpEntity,
                BaseResponse.class
        );
        if (Objects.requireNonNull(exchange.getBody()).getData() != null) {
            return "saved";
        }
        return "wrong";
    }

    public BasketResponseDTO basketsOfUser(UUID userId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(backendHost + "/basketController/basketsOfUser")
                .queryParam("userId", userId);
        ResponseEntity<BaseResponse<BasketResponseDTO>> exchange = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<BasketResponseDTO>>() {}
        );
        return exchange.getBody().getData();
    }

    public BasketResponseDTO updateCount(UUID productId, UUID basketId, int count) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(backendHost + "/basketController/updateCount/"+productId)
                .queryParam("productId", productId)
                .queryParam("basketId", basketId)
                .queryParam("count", count);

        ResponseEntity<BaseResponse<BasketResponseDTO>> exchange = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<BasketResponseDTO>>() {}
        );
        return exchange.getBody().getData();
    }
}
