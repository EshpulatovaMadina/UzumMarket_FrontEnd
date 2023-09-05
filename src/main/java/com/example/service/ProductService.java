package com.example.service;

import com.example.dto.BaseResponse;
import com.example.exception.DataAlreadyExistsException;
import com.example.model.CategoryResponseDTO;
import com.example.model.ProductCreateDTO;
import com.example.model.ProductResponseDTO;
import com.example.model.RestResponsePage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public String create (ProductCreateDTO dto){
        HttpEntity<ProductCreateDTO> dtoHttpEntity = new HttpEntity<>(dto);
        try {
            ResponseEntity<BaseResponse<ProductResponseDTO>> exchange = restTemplate.exchange(
                    backendHost + "/product/create",
                    HttpMethod.POST,
                    dtoHttpEntity,
                    new ParameterizedTypeReference<BaseResponse<ProductResponseDTO>>() {}
            );
            return "created";
        }catch (HttpStatusCodeException e ) {

            DataAlreadyExistsException response = e.getResponseBodyAs(DataAlreadyExistsException.class);
            System.out.println("e.getResponseBodyAsString() = " + response );
            assert response != null;
            return Objects.requireNonNull(response.getMessage());
        }

    }

    public List<ProductResponseDTO> getAll(UUID sellerId,UUID categoryId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(backendHost + "/product/get-all")
                .queryParam("sellerId", sellerId)
                .queryParam("categoryId", categoryId);

        ResponseEntity<BaseResponse<List<ProductResponseDTO>>> exchange = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<List<ProductResponseDTO>>>() {}
        );
        return exchange.getBody().getData();
    }

    public List<ProductResponseDTO> getAllByCategory(UUID sellerId , UUID categoryId) {
        ResponseEntity<BaseResponse<List<ProductResponseDTO>>> exchange = restTemplate.exchange(
                backendHost + "/product/get-by-category/" + categoryId +"/"+sellerId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<List<ProductResponseDTO>>>() {}
                );
        return exchange.getBody().getData();
    }
}
