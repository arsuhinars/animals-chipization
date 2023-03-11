package com.arsuhinars.animals_chipization.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Configuration
public class AnimalsChipizationConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return builder -> builder
            .serializerByType(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
                @Override
                public void serialize(
                    OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider
                ) throws IOException {
                    jsonGenerator.writeString(
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(
                            offsetDateTime.truncatedTo(ChronoUnit.SECONDS)
                        )
                    );
                }
            })
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
