package com.example.service;

import com.example.dto.BaseResponse;
import com.example.model.CategoryCreateDTO;
import com.example.model.CategoryResponseDTO;
import com.example.model.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;
    private final AttachmentService attachmentService;


    public String save(String name, UUID parentId, File file) {
        UUID uuid = attachmentService.create(file);
//        attachmentService.download(uuid);
        CategoryCreateDTO dto = new CategoryCreateDTO(name, uuid, parentId, true);

        HttpEntity<CategoryCreateDTO> dtoHttpEntity = new HttpEntity<>(dto);

        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/category/create",
                HttpMethod.POST,
                dtoHttpEntity,
                BaseResponse.class
        );
        if (Objects.requireNonNull(exchange.getBody()).getData() != null) {
            return "saved";
        }
        return "wrong";
    }

    public Page<CategoryResponseDTO> getFirstCategories() {
        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/category/first",
                HttpMethod.POST,
                null,
                BaseResponse.class
        );
        return (Page<CategoryResponseDTO>) exchange.getBody().getData();
    }

    public Page<CategoryResponseDTO> getSubCategories(UUID parentId) {
        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/subCategories/" + parentId,
                HttpMethod.POST,
                null,
                BaseResponse.class
        );
        return (Page<CategoryResponseDTO>) exchange.getBody().getData();
    }
}
