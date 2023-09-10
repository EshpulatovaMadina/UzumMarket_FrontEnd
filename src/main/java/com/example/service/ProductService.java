package com.example.service;

import com.example.dto.BaseResponse;
import com.example.exception.DataAlreadyExistsException;
import com.example.model.CreateDTO.ProductCreateDTO;
import com.example.model.ResponceDTO.ProductResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
            return null;
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

    public List<ProductResponseDTO> getAllByCategory( UUID categoryId) {
        ResponseEntity<BaseResponse<List<ProductResponseDTO>>> exchange = restTemplate.exchange(
                backendHost + "/product/get-by-category/" + categoryId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<List<ProductResponseDTO>>>() {}
                );
        return exchange.getBody().getData();
    }

    public List<ProductResponseDTO> search(String word) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(backendHost + "/product/search")
                .queryParam("word", word);

        ResponseEntity<BaseResponse<List<ProductResponseDTO>>> exchange = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<List<ProductResponseDTO>>>() {
                }
        );
        return exchange.getBody().getData();
    }

    public ProductResponseDTO getById(UUID productId) {
        ResponseEntity<BaseResponse<ProductResponseDTO>> exchange = restTemplate.exchange(
                backendHost + "/product/getById/" + productId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BaseResponse<ProductResponseDTO>>() {}
        );
        return exchange.getBody().getData();
    }
}
