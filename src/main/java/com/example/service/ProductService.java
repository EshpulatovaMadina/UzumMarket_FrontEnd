package com.example.service;

import com.example.model.ProductCreateDTO;
import com.example.model.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public String create (ProductCreateDTO dto){
        HttpEntity<ProductCreateDTO> dtoHttpEntity = new HttpEntity<>(dto);
        ResponseEntity<ProductResponseDTO> exchange = restTemplate.exchange(
                backendHost + "/product/create",
                HttpMethod.POST,
                dtoHttpEntity,
                ProductResponseDTO.class
        );
        if(exchange.getBody() == null){
            return "success";
        }
        return "failed";
    }

    public List<ProductResponseDTO> getAll(UUID sellerId) {
        HttpEntity<UUID> id = new HttpEntity<>(sellerId);
        ResponseEntity<TypeReference> exchange = restTemplate.exchange(
                backendHost + "/product/get-all",
                HttpMethod.GET,
                id,
                TypeReference.class
        );
        return (List<ProductResponseDTO>) exchange.getBody();
    }
}
