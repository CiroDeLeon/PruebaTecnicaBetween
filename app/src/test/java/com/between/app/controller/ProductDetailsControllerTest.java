package com.between.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.between.app.dto.ProductDetail;
import com.between.app.service.SimilarProductsService;

import feign.FeignException;

@SpringBootTest
public class ProductDetailsControllerTest {
    @Mock
    private SimilarProductsService productService;
    @InjectMocks
    private ProductDetailsController testService;
    @Test
    public void getSimilarProductsOk() {
        //ProductDetail p1 = new ProductDetail("1", "Shirt", 9.99, true);
        ProductDetail p2 = new ProductDetail("2", "Dress", 19.99, true);
        ProductDetail p3 = new ProductDetail("3", "Blazzer", 29.99, true);
        ProductDetail p4 = new ProductDetail("4", "Boots", 39.99, true);
        Set<ProductDetail> products = Set.of(p2,p3,p4);
        when(productService.getSimilarProducts("1")).thenReturn(products);

        ResponseEntity<Set<ProductDetail>> actual = testService.getSimilarProducts("1");
        
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(products, actual.getBody());
    }

    @Test
    public void getSimilarProductsWhenNotFound404() {
    	
        when(productService.getSimilarProducts("1")).thenThrow(FeignException.class);

        try {
            testService.getSimilarProducts("1");
            fail();
        } catch (Exception e) {
            assertEquals(FeignException.class, e.getClass());
        }
    }

    @Test
    public void getSimilarProductsThrowException500() {
    	
        when(productService.getSimilarProducts("1"))
                .thenThrow(FeignException.InternalServerError.class);

        try {
            testService.getSimilarProducts("1");
            fail();
        } catch (Exception e) {
            assertEquals(FeignException.InternalServerError.class, e.getClass());
        }
    }
}
