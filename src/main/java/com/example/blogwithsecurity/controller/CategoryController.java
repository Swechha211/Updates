package com.example.blogwithsecurity.controller;

import com.example.blogwithsecurity.entity.Category;
import com.example.blogwithsecurity.service.CategoryService;
import com.example.blogwithsecurity.utils.ApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category createCategory =this.categoryService.createCategory(category);
        return new ResponseEntity<Category>(createCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{catId}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category categoryDto, @PathVariable Integer catId){
        Category updatedCategory =this.categoryService.updateCategory(categoryDto, catId);
        return new ResponseEntity<Category>(updatedCategory, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping ("/{catId}")
    public ResponseEntity<ApiResource> deleteCategory(@PathVariable Integer catId){
        this.categoryService.deleteCategory( catId);
        return new ResponseEntity<ApiResource>(new ApiResource("category is deleted", true), HttpStatus.OK);
    }

    @GetMapping ("/{catId}")
    public ResponseEntity<Category> getCategory(@PathVariable Integer catId){
        Category categoryDto = this.categoryService.getCategory(catId);
        return new ResponseEntity<Category>(categoryDto, HttpStatus.OK);
    }

    @GetMapping ("/")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = this.categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

}
