package nl.helvar.servicetickets.configurations.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule customLocalDateTimeModule = new SimpleModule();
        customLocalDateTimeModule.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        customLocalDateTimeModule.addSerializer(LocalDate.class, new CustomLocalDateSerializer());

        objectMapper.registerModule(customLocalDateTimeModule);

        return objectMapper;
    }
}