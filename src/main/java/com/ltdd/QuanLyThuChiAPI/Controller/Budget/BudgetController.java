package com.ltdd.QuanLyThuChiAPI.Controller.Budget;

import com.ltdd.QuanLyThuChiAPI.Request.Budget.BudgetRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Service.Budget.BudgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(value = "/api/budget")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllBudget(Principal principal)
    {
        return budgetService.getAll(principal.getName());
    }

    @GetMapping(value = "/all/inmonth")
    public ApiResponse<Object> getAllBudgetInMonth(Principal principal)
    {
        return budgetService.getAllInMonth(principal.getName());
    }
    @GetMapping(value = "/{id}")
    public ApiResponse<Object> getById(Principal principal, @PathVariable("id") Long id)
    {
        return budgetService.getBudgetById(principal.getName(), id);
    }

    @Transactional
    @PostMapping(value = "/add/{idCategory}")
    public ApiResponse<Object> addBudget(Principal principal, @PathVariable("idCategory") Long idCategory, @RequestBody BudgetRequest budgetRequest)
    {
        return budgetService.addBudget(principal.getName(), idCategory, budgetRequest);
    }

    @PutMapping(value = "/update/{id}")
    public ApiResponse<Object> updateBudget(Principal principal, @PathVariable("id") Long id, @RequestBody BudgetRequest budgetRequest)
    {
        return budgetService.updateBudget(principal.getName(), id, budgetRequest);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiResponse<Object> deleteBudget(Principal principal, @PathVariable("id") Long id)
    {
        return budgetService.deleteBudget(principal.getName(), id);
    }
}
