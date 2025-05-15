package com.ltdd.QuanLyThuChiAPI.Request.Category;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String description;

    private String name;

    private String color;

    private Long type;

    private String updated_at;
}
