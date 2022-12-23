package com.between.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.between.app.clients.ProductDetailClient;
import com.between.app.dto.ProductDetail;

import feign.FeignException;

@SpringBootTest
public class SimilarProductsServiceTest {
   @InjectMocks
   SimilarProductsService testService;	
   @Mock
   ProductDetailClient client;
   @Test
   public void shouldGetSimilarProductsOk() {
       ProductDetail p1 = new ProductDetail("1", "Shirt", 9.99, true);
       ProductDetail p2 = new ProductDetail("2", "Dress", 19.99, true);
       Set<String> products = Set.of("3","4","2");
       when(client.getSimilarProductIDs("1")).thenReturn(products);
       when(client.getProduct("1")).thenReturn(p1);
       when(client.getProduct("2")).thenReturn(p2);

       Set<ProductDetail> actual = testService.getSimilarProducts("1");

       assertEquals(3, actual.size());
       assertTrue(actual.contains(p1));
       assertTrue(actual.contains(p2));
   }

   @Test
   public void shouldReturnNotFoundGetSimilarProductsWhenNotFoundProduct() {
       ProductDetail p1 = new ProductDetail("1", "Shirt", 9.99, true);
       Set<String> products = Set.of("3","4","2");
       when(client.getSimilarProductIDs("1")).thenReturn(products);
       when(client.getProduct("1")).thenReturn(p1);
       when(client.getProduct("2")).thenThrow(FeignException.NotFound.class);

       try {
           testService.getSimilarProducts("1");
       } catch (Exception e) {
           assertEquals(FeignException.NotFound.class, e.getClass());
       }
   }

   @Test
   public void shouldReturnExceptionGetSimilarProductsWhenInternalServerError() {
       ProductDetail p1 = new ProductDetail("1", "Shirt", 9.99, true);
       Set<String> products = Set.of("3","4","2");
       when(client.getSimilarProductIDs("1")).thenReturn(products);
       when(client.getProduct("1")).thenReturn(p1);
       when(client.getProduct("2")).thenThrow(FeignException.InternalServerError.class);

       try {
           testService.getSimilarProducts("1");
       } catch (Exception e) {
           assertEquals(FeignException.InternalServerError.class, e.getClass());
       }
   }
}
