package com.between.app.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.between.app.dto.ProductDetail;
import com.between.app.service.SimilarProductsService;

import feign.FeignException;

@RestController
public class ProductDetailsController {
   SimilarProductsService service;
   @Autowired
   public ProductDetailsController(SimilarProductsService service) {
	   this.service=service;
   }
   @GetMapping("/product/{productId}/similar")
   public ResponseEntity<Set<ProductDetail>> getSimilarProducts(@PathVariable String productId) {
       return ResponseEntity.ok(
               service.getSimilarProducts(productId)
       );
   }

   @ExceptionHandler(FeignException.NotFound.class)
   public ResponseEntity<String> handleFeignException() {
       return ResponseEntity
               .status(HttpStatus.NOT_FOUND)
               .body("Product not found");
   }
}
