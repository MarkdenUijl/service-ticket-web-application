package nl.helvar.servicetickets.configurations.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.helvar.servicetickets.configurations.jackson.CustomLocalDateSerializer;
import nl.helvar.servicetickets.configurations.jackson.CustomLocalDateTimeSerializer;
import org.springframework.context.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Exclude null values from the JSON response
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Register the JavaTimeModule to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());

        // Register the custom serializer for LocalDateTime
        SimpleModule customLocalDateTimeModule = new SimpleModule();
        customLocalDateTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());

        // Register the custom serializer for LocalDate
        customLocalDateTimeModule.addSerializer(LocalDate.class, new CustomLocalDateSerializer());

        objectMapper.registerModule(customLocalDateTimeModule);

        return objectMapper;
    }
}