package com.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final RestTemplate restTemplate;
    @Value("${backend.host}")
    private String backendHost;

    public UUID create(File file){
        HttpEntity<File> fileHttpEntity = new HttpEntity<>(file);
        ResponseEntity<UUID> exchange = restTemplate.exchange(
                backendHost + "/image/single-upload",
                HttpMethod.POST,
                fileHttpEntity,
                UUID.class
        );
        return exchange.getBody();
    }
    public List<UUID> multipleUpload(File file){
        HttpEntity<File> fileHttpEntity = new HttpEntity<>(file);
        ResponseEntity<List<UUID>> exchange = restTemplate.exchange(
                backendHost + "/image/multiple-upload",
                HttpMethod.POST,
                fileHttpEntity,
                new ParameterizedTypeReference<List<UUID>>() {}
        );
        return exchange.getBody();
    }
}
