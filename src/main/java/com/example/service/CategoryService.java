package com.example.service;

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

        ResponseEntity<CategoryResponseDTO> exchange = restTemplate.exchange(
                backendHost + "/category/create",
                HttpMethod.POST,
                dtoHttpEntity,
                CategoryResponseDTO.class
        );
        if (exchange.getBody() != null) {
            return "saved";
        }
        return "wrong";
    }

    public Page<CategoryResponseDTO> getFirstCategories() {
        return null;
    }

    public Page<CategoryResponseDTO> getSubCategories(UUID parentId) {
        ResponseEntity<Page<CategoryResponseDTO>> exchange = restTemplate.exchange(
                backendHost + "/subCategories/" + parentId,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<Page<CategoryResponseDTO>>() {
                } // Parametrizatsiya turi
        );
        return exchange.getBody();
    }
}
