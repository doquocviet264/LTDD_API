package com.ltdd.QuanLyThuChiAPI.Interface.Budget;

import com.ltdd.QuanLyThuChiAPI.Request.Budget.BudgetRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;

public interface BudgetInterface {

    public ApiResponse<Object> getAll(String username);

    public ApiResponse<Object> addBudget(String username, Long idCategory, BudgetRequest budgetRequest);

    public ApiResponse<Object> updateBudget(String username, Long idBudget, BudgetRequest budgetRequest);

    public ApiResponse<Object> deleteBudget(String username, Long idBudget);

    public ApiResponse<Object> getBudgetById(String username, Long idBudget);

    public ApiResponse<Object> getAllInMonth(String username);
    public ApiResponse<Object> getBudgetByCategory(String username, Long idCategory);
}
