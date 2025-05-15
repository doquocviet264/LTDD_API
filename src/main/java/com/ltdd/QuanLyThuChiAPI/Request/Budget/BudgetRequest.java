package com.ltdd.QuanLyThuChiAPI.Request.Budget;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class BudgetRequest {

    private Long amount;

    private String description;
}
