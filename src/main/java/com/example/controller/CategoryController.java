package com.example.controller;

import com.example.model.CategoryResponseDTO;
import com.example.model.UserResponseDTO;
import com.example.service.CategoryService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @PostMapping("/create/{userId}")
    public String create
            (
                    @RequestParam(name = "name") String name,
                    @RequestParam(required = false ) UUID parentId,
                    @RequestParam MultipartFile img,
                    @PathVariable UUID userId,
                    Model model
            ) throws IOException {
        UserResponseDTO user = userService.getById(userId);
        model.addAttribute("user",user);
        categoryService.save(name, parentId, img);
        return "/admin/menu";
    }
    @GetMapping("/get/{userId}")
    public String get(Model model, @PathVariable UUID userId){
        model.addAttribute("parentId",null);
        UserResponseDTO user = userService.getById(userId);
        model.addAttribute("user", user);
        return "admin/first-category";
    }

    @GetMapping("/first/{userId}")
    public String getFirstCategory(
            @PathVariable UUID userId,
            Model model
    ) {
        UserResponseDTO user = userService.getById(userId);
        Page<CategoryResponseDTO> responseDTOS = categoryService.getFirstCategories();
        model.addAttribute("categories", responseDTOS);
        model.addAttribute("user", user);
        model.addAttribute("parentId",null);
        switch (user.getUserRole()) {
            case "USER"-> {
                return "user/category";
            }
            case "ADMIN" -> {
                return "first-category";
            }
            case "SELLER" -> {
                return "seller/category";
            }
            default -> {
                return "index";
            }
        }
    }

    @GetMapping("/sub/{userId}/{categoryId}")
    public String getSubCategory(
            @PathVariable UUID userId,
            @PathVariable UUID categoryId,
            Model model
    ) {
        UserResponseDTO user = userService.getById(userId);
        Page<CategoryResponseDTO> responseDTOS = categoryService.getSubCategories(categoryId);
        model.addAttribute("categories", responseDTOS);
        model.addAttribute("user", user);
        model.addAttribute("parentId",categoryId);
        switch (user.getUserRole()) {
            case "USER"-> {
                return "user/category";
            }
            case "ADMIN" -> {
                return "first-category";
            }
            case "SELLER" -> {
                return "seller/category";
            }
            default -> {
                return "index";
            }
        }
    }
}
