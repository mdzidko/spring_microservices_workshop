package com.mdzidko.ordering.customersorders.product;

import com.mdzidko.ordering.customersorders.HttpErrorExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

class ProductsResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(final ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().is4xxClientError() ||
                clientHttpResponse.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(final ClientHttpResponse clientHttpResponse) throws IOException {
        if(clientHttpResponse.getStatusCode() == HttpStatus.NOT_FOUND){
            throw new ProductNotFoundException(HttpErrorExtractor.getMessage(clientHttpResponse));
        }
        else if(clientHttpResponse.getStatusCode() == HttpStatus.BAD_REQUEST){
            throw new NotEnoughProductException(HttpErrorExtractor.getMessage(clientHttpResponse));
        }
        else{
            throw new RuntimeException(HttpErrorExtractor.getMessage(clientHttpResponse));
        }
    }
}
