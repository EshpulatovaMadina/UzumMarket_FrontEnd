package com.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.example.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public UUID create(MultipartFile file) {
        HttpEntity<MultipartFile> fileHttpEntity = new HttpEntity<>(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("image", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e) {
            return null;
        }


        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BaseResponse> exchange = restTemplate.exchange(
                backendHost + "/image/single-upload",
                HttpMethod.POST,
                requestEntity,
                BaseResponse.class
        );
        System.out.println("exchange.getBody().getData() = " + exchange.getBody().getData());
        return UUID.fromString((String) exchange.getBody().getData());
    }

    public BaseResponse<List<UUID>> multipleUpload(MultipartFile[] imgs) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile img : imgs) {
            ByteArrayResource resource = new ByteArrayResource(img.getBytes()) {
                @Override
                public String getFilename() {
                    return img.getOriginalFilename();
                }
            };
            body.add("files", resource);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BaseResponse<List<UUID>>> exchange = restTemplate.exchange(
                backendHost + "/image/multiple-upload",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<BaseResponse<List<UUID>>>() {
                }
        );
        return exchange.getBody();
    }

}
