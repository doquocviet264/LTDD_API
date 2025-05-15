package com.ltdd.QuanLyThuChiAPI.Interface.Category;

import com.ltdd.QuanLyThuChiAPI.Model.Category.CategoryModel;
import com.ltdd.QuanLyThuChiAPI.Request.Category.CategoryRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;

import java.util.List;

public interface CategoryInterface {
    public ApiResponse<Object> addCategory(String username, CategoryRequest categoryRequest);

    public ApiResponse<Object> updateCategory(String username, CategoryRequest categoryRequest, Long idCategory);
    public CategoryModel getCategory(String username, Long categoryId);
    public Iterable<CategoryModel> getAllCategory(String username);
    public List<CategoryModel> expenseCategory(String username);

    public ApiResponse<Object> deleteCategory(String username, Long categoryId);
    public List<CategoryModel> imcomeCategory(String username);
}
