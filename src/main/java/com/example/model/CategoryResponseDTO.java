package com.example.model;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private boolean active;

    private UUID photoId;
    private UUID parentId;
}
