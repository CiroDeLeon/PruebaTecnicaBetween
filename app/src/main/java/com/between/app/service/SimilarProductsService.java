package com.between.app.service;

import java.util.HashSet;
import java.util.Set;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.between.app.clients.ProductDetailClient;
import com.between.app.dto.ProductDetail;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import jakarta.annotation.PostConstruct;

@Service
public class SimilarProductsService {

  
  private CacheManager cacheManager;

  
  private Cache<Object,Object> similarProductsCache;
  
  
  private ProductDetailClient client;
  @Autowired
  public SimilarProductsService(ProductDetailClient client,CacheManager cacheManagerDef) {
      this.client = client;
      this.cacheManager=cacheManagerDef;
  }

  @PostConstruct
  public void init() {
    similarProductsCache = cacheManager.getCache("similarProductsCache",Object.class,Object.class);
  }



@HystrixCommand(fallbackMethod = "getSimilarProductsFallback",
commandProperties = {
    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
})
public Set<ProductDetail> getSimilarProducts(String productId) {
    @SuppressWarnings("unchecked")
	Set<ProductDetail> similarProducts=(Set<ProductDetail>)similarProductsCache.get(productId);
    if (similarProducts == null) {      
      Set<String> similarProductIDs = client.getSimilarProductIDs(productId);
      similarProducts = new HashSet<>();
      for (String id : similarProductIDs) {
        ProductDetail p = client.getProduct(id);
        similarProducts.add(p);
      }
      similarProductsCache.put(productId, similarProducts);
    }
    return similarProducts;
  }
public Set<ProductDetail> getSimilarProductsFallback() {
	return new HashSet<>();
}
}

