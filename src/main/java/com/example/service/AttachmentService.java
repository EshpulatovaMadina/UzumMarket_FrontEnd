package com.example.service;

import com.example.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public UUID create(MultipartFile file) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> fileHttpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<BaseResponse<UUID>> exchange = restTemplate.exchange(
                backendHost + "/image/single-upload",
                HttpMethod.POST,
                fileHttpEntity,
                new ParameterizedTypeReference<BaseResponse<UUID>>() {
                }
        );
        return Objects.requireNonNull(exchange.getBody()).getData();
    }

    public List<UUID> multipleUpload(File file) {
        HttpEntity<File> fileHttpEntity = new HttpEntity<>(file);
        ResponseEntity<List<UUID>> exchange = restTemplate.exchange(
                backendHost + "/image/multiple-upload",
                HttpMethod.POST,
                fileHttpEntity,
                new ParameterizedTypeReference<List<UUID>>() {
                }
        );
        return exchange.getBody();
    }
}
