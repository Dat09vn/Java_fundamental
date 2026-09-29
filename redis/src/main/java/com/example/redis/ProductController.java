package com.example.redis;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<Product> create(@RequestBody Product product) {
		Product savedProduct = productService.save(product);
		return ResponseEntity.created(URI.create("/products/" + savedProduct.id())).body(savedProduct);
	}

	@GetMapping
	public List<Product> findAll() {
		return productService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> findById(@PathVariable String id) {
		Product product = productService.findById(id);
		return product == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(product);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		return productService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}