package com.ltdd.QuanLyThuChiAPI.Request.Category;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryRequest {
    private String description;
    private String name;
    private String color;
    private Long type;
}
