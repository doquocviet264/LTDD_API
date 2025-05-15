package com.ltdd.QuanLyThuChiAPI.Controller.Category;


import com.ltdd.QuanLyThuChiAPI.Model.Category.CategoryModel;
import com.ltdd.QuanLyThuChiAPI.Request.Category.CategoryRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Service.Category.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;


@Slf4j
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/{id}")
    public Mono<CategoryModel> getCategory(Principal principal, @PathVariable("id") Long id)
    {
        return Mono.just(categoryService.getCategory(principal.getName(), id));
    }
    @PostMapping(value = "/add")
    @Transactional
    public Mono<ApiResponse<Object>> addCategory(Principal principal, @RequestBody CategoryRequest categoryRequest)
    {

        return Mono.just(categoryService.addCategory(principal.getName(), categoryRequest));
    }

    @PutMapping(value = "/update/{id}")
    public Mono<ApiResponse<Object>> updateCategory(Principal principal, @RequestBody CategoryRequest categoryRequest, @PathVariable("id") Long categoryId) {

        return Mono.just(categoryService.updateCategory(principal.getName(), categoryRequest, categoryId));
    }

    @DeleteMapping(value = "delete/{id}")
    public Mono<ApiResponse<Object>> deleteCategory(Principal principal, @PathVariable("id") Long categoryId)
    {
        return Mono.just(categoryService.deleteCategory(principal.getName(), categoryId));
    }

    // lấy ra các danh mục cho việc thu tiền về
    @GetMapping(value = "/income")
    public Mono<List<CategoryModel>> imcomeCategory(Principal principal)
    {
        return Mono.just(categoryService.imcomeCategory(principal.getName()));
    }

    // lấy ra các danh mục cho việc chi tiền
    @GetMapping(value = "/expense")
    public Mono<List<CategoryModel>> expenseCategory(Principal principal)
    {
        return Mono.just(categoryService.expenseCategory(principal.getName()));
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public Iterable<CategoryModel> getCategory(Principal principal)
    {
        return categoryService.getAllCategory(principal.getName());
    }
}
