package com.arsuhinars.animals_chipization.location.service;

import com.arsuhinars.animals_chipization.core.exception.AppException;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public interface GeoHashService {
    enum GeoHashVersion {
        V1, V2, PROTECTED_V3
    }

    String getGeoHash(@NotNull GeoPosition position, @NonNull GeoHashVersion version) throws AppException;
}
