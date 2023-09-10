package com.example.controller;

import com.example.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;


    @PostMapping("/create")
    public String create(
            @RequestParam UUID productId,
            @RequestParam UUID userId,
            @RequestParam int rate,
            @RequestParam String text,
            Model model) {

        String s = feedbackService.create(productId, userId, rate, text);
        model.addAttribute("feedbacks", feedbackService.feedbackOfProduct(userId));
        return "redirect:/product/getById?productId=" + productId + "&userId=" + userId;
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam UUID productId,
            @RequestParam UUID feedbackId,
            @RequestParam UUID userId,
            Model model) {
        feedbackService.delete(feedbackId);
        return "redirect:/product/getById?productId=" + productId + "&userId=" + userId;
    }


}
