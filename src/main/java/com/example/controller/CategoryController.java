package com.example.controller;

import com.example.model.ResponceDTO.CategoryResponseDTO;
import com.example.model.ResponceDTO.ProductResponseDTO;
import com.example.model.ResponceDTO.UserResponseDTO;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.UserService;
import jakarta.servlet.annotation.MultipartConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
@MultipartConfig(maxFileSize = 5000000, maxRequestSize = 20, fileSizeThreshold = 1024)
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    @PostMapping("/create")
    public String create
            (
                    @RequestParam String name,
                    @RequestParam(required = false ) UUID parentId,
                    @RequestParam MultipartFile img,
                    @RequestParam UUID userId,
                    Model model
            ) {
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
        List<CategoryResponseDTO> responseDTOS = categoryService.getFirstCategories();
        model.addAttribute("categories", responseDTOS);
        model.addAttribute("user", user);
        model.addAttribute("parentId",null);
        switch (user.getUserRole()) {
            case "USER"-> {
                return "user/category";
            }
            case "ADMIN" -> {
                return "admin/first-category";
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
        List<CategoryResponseDTO> responseDTOS = categoryService.getSubCategories(categoryId);
        model.addAttribute("categories", responseDTOS);
        model.addAttribute("user", user);
        model.addAttribute("parentId",categoryId);
        switch (user.getUserRole()) {
            case "USER"-> {
                if(responseDTOS.isEmpty()){
                    List<ProductResponseDTO> products = productService.getAll(userId,categoryId);
                    model.addAttribute("products",products);
                    model.addAttribute("msg",null);
                    return "user/products";
                }
                return "user/category";
            }
            case "ADMIN" -> {
                return "admin/sub-category";
            }
            case "SELLER" -> {
                if(responseDTOS.isEmpty()){
                    List<ProductResponseDTO> products = productService.getAll(userId,categoryId);
                    model.addAttribute("products",products);
                    model.addAttribute("msg",null);
                    return "seller/products";
                }
                return "seller/category";
            }
            default -> {
                return "index";
            }
        }
    }
}
