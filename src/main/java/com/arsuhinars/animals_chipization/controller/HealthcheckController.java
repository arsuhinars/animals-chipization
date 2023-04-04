package com.arsuhinars.animals_chipization.controller;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
public class HealthcheckController {
    @GetMapping("/status")
    public String checkStatus() {
        return "OK";
    }
}
