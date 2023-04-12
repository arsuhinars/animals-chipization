package com.arsuhinars.animals_chipization.location.controller;

import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import com.arsuhinars.animals_chipization.location.service.GeoHashService;
import jakarta.validation.Valid;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/locations", produces = MimeTypeUtils.TEXT_PLAIN_VALUE)
@Validated
public class GeoHashController {
    private final GeoHashService service;

    public GeoHashController(GeoHashService service) {
        this.service = service;
    }

    @GetMapping("/geohash")
    public String getGeoHashV1(@Valid GeoPosition position) {
        return service.getGeoHash(position, GeoHashService.GeoHashVersion.V1);
    }

    @GetMapping("/geohashv2")
    public String getGeoHashV2(@Valid GeoPosition position) {
        return service.getGeoHash(position, GeoHashService.GeoHashVersion.V2);
    }

    @GetMapping("/geohashv3")
    public String getProtectedGeoHashV3(@Valid GeoPosition position) {
        return service.getGeoHash(position, GeoHashService.GeoHashVersion.PROTECTED_V3);
    }
}
