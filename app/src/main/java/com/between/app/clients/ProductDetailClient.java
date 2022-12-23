package com.between.app.clients;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.between.app.dto.ProductDetail;

@FeignClient(name = "SimilarProductsClient", url = "localhost:3001")
public interface ProductDetailClient {
	@GetMapping("/product/{productId}/similarids")
	Set<String> getSimilarProductIDs(@PathVariable String productId);

	@GetMapping("/product/{productId}")
	ProductDetail getProduct(@PathVariable String productId);  
}
