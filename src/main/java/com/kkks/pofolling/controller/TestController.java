package com.kkks.pofolling.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "✅ 프론트와 백엔드 연동 성공!";
    }
}
