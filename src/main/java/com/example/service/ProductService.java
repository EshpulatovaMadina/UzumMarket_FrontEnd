package com.example.service;

import com.example.dto.BaseResponse;
import com.example.model.ProductCreateDTO;
import com.example.model.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public String create (ProductCreateDTO dto){
        HttpEntity<ProductCreateDTO> dtoHttpEntity = new HttpEntity<>(dto);
        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/product/create",
                HttpMethod.POST,
                dtoHttpEntity,
                BaseResponse.class
        );
        if(Objects.requireNonNull(exchange.getBody()).getData() == null){
            return "success";
        }
        return "failed";
    }

    public Page<ProductResponseDTO> getAll(UUID sellerId) {
        HttpEntity<UUID> id = new HttpEntity<>(sellerId);
        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/product/get-all",
                HttpMethod.GET,
                id,
                BaseResponse.class
        );
        return (Page<ProductResponseDTO>) exchange.getBody().getData();
    }
}
