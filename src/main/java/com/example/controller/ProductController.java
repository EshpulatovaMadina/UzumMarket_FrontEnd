package com.example.controller;

import com.example.dto.BaseResponse;
import com.example.model.CreateDTO.ProductCreateDTO;
import com.example.model.ResponceDTO.FeedbackResponseDTO;
import com.example.model.ResponceDTO.ProductResponseDTO;
import com.example.service.AttachmentService;
import com.example.service.FeedbackService;
import com.example.service.ProductService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private final AttachmentService attachmentService;
    private final FeedbackService feedbackService;

    @PostMapping("/create")
    public String create(
                    @RequestParam("image") MultipartFile [] file,
                    @ModelAttribute ProductCreateDTO dto,
                    Model model) throws IOException {
                 BaseResponse<List<UUID>> list = attachmentService.multipleUpload( file);
                 dto.setPhotos(list.getData());
                 String msg = productService.create(dto);
                 List<ProductResponseDTO> products = productService.getAll(dto.getSellerId(),dto.getCategoryId());
        model.addAttribute("products",products);
        model.addAttribute("parentId",dto.getCategoryId());
        model.addAttribute("user",userService.getById(dto.getSellerId()));
        model.addAttribute("msg", msg);
        return "seller/products";
    }

    @GetMapping("search")
    public String search(
            @RequestParam String word,
            Model model) {
        List<ProductResponseDTO> responseDTOS = productService.search(word);
        model.addAttribute("products",responseDTOS);
        model.addAttribute("msg",null);
        return "user/products";
    }
    @GetMapping("getById")
    public String getById(
        @RequestParam UUID productId,
        @RequestParam UUID userId,
        Model model
    ){
        ProductResponseDTO responseDTO = productService.getById(productId);
        List<FeedbackResponseDTO> feedbacks = feedbackService.feedbackOfProduct(productId);
        model.addAttribute("userId",userId);
        model.addAttribute("product" , responseDTO);
        model.addAttribute("feedbacks", feedbacks);
        return "user/oneProductPage";
    }
}
