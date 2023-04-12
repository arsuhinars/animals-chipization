package com.arsuhinars.animals_chipization.location.service;

import ch.hsr.geohash.GeoHash;
import com.arsuhinars.animals_chipization.core.exception.AppException;
import com.arsuhinars.animals_chipization.core.exception.InvalidFormatException;
import com.arsuhinars.animals_chipization.core.exception.UnresolvedException;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

@Service
public class GeoHashServiceImpl implements GeoHashService {
    @Override
    public String getGeoHash(GeoPosition position, @NonNull GeoHashVersion version) throws AppException {
        var geohash = GeoHash.withCharacterPrecision(
            position.getLatitude(), position.getLongitude(), 12
        ).toBase32();

        switch (version) {
            case V1 -> {
                return geohash;
            }
            case V2 -> {
                var encoded = geohash.getBytes(StandardCharsets.UTF_8);
                return Base64.getEncoder().encodeToString(encoded);
            }
            case PROTECTED_V3 -> {
                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException ex) {
                    throw new UnresolvedException();
                }
                var digest = md.digest(geohash.getBytes(StandardCharsets.UTF_8));
                for (int i = 0; i < digest.length / 2; ++i) {
                    byte temp = digest[i];
                    digest[i] = digest[digest.length - i - 1];
                    digest[digest.length - i - 1] = temp;
                }
                return Base64.getEncoder().encodeToString(digest);
            }
            default -> throw new InvalidFormatException("Unsupported geo hash version");
        }
    }
}
