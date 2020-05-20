package com.mdzidko.ordering.customersorders;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class HttpErrorExtractor{
    public static String getMessage(final ClientHttpResponse clientHttpResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper
                .readValue(clientHttpResponse.getBody(), ErrorMessage.class)
                .getMessage();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private class ErrorMessage {
        private String timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }
}



