package com.ltdd.QuanLyThuChiAPI.Response.Category;

import com.ltdd.QuanLyThuChiAPI.Model.Category.CategoryModel;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryResponse {
    private boolean status;

    private String message;

    private CategoryModel categoryModel;
}
