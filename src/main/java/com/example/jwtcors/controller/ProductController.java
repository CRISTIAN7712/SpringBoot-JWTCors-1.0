package com.example.jwtcors.controller;

import com.example.jwtcors.entity.Product;
import com.example.jwtcors.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRODUCTS')")
    public List<Product> getAll() {
        return repository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PRODUCTS')")
    public Product create(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PRODUCTS')")
    public Product update(@PathVariable Long id, @RequestBody Product newData) {
        Product p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        p.setName(newData.getName());
        p.setPrice(newData.getPrice());
        p.setStock(newData.getStock());

        return repository.save(p);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCTS')")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

