package com.ezen.boot_JPA.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/gallery/*")
public class GalleryController {
    @GetMapping("/choice")
    public  void choice() {}

    @GetMapping("/seoul")
    public  void seoul() {}

    @GetMapping("/japan")
    public  void japan() {}

    @GetMapping("/travel")
    public  void travel() {}
}
