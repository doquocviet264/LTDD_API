package com.ltdd.QuanLyThuChiAPI.Controller.Goal;

import com.ltdd.QuanLyThuChiAPI.Request.Goal.DepositRequest;
import com.ltdd.QuanLyThuChiAPI.Request.Goal.GoalRequest;
import com.ltdd.QuanLyThuChiAPI.Request.Goal.UpdateGoalRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Service.Goal.GoalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(value = "/api/goal")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAllGoals(Principal principal)
    {
        return goalService.getAllGoals(principal.getName());
    }

    @Transactional
    @PostMapping(value = "/add")
    public Mono<ApiResponse<Object>> addGoal(Principal principal,@RequestBody GoalRequest goalRequest) throws ParseException {
        return Mono.just(goalService.addGoal(principal.getName(), goalRequest));
    }

    @DeleteMapping(value = "/delete/{id}")
    public Mono<ApiResponse<Object>> deleteGoal(Principal principal, @PathVariable("id") Long idGoal)
    {
        return Mono.just(goalService.deleteGoal(principal.getName(), idGoal));
    }

    @PutMapping(value = "/update/{id}")
    public Mono<ApiResponse<Object>> updateGoal(Principal principal, @RequestBody UpdateGoalRequest updateGoalRequest, @PathVariable("id") Long idGoal) {
        return Mono.just(goalService.updateGoal(principal.getName(), updateGoalRequest, idGoal));
    }

    @PutMapping(value = "/update-deposit/{id}")
    public Mono<ApiResponse<Object>> updateDeposit(Principal principal, @RequestBody Map<String, Object> depositMap, @PathVariable("id") Long idGoal)
    {
        Long deposit = Long.parseLong(depositMap.get("deposit").toString());
        DepositRequest depositRequest = DepositRequest.builder().deposit(deposit).build();
        return Mono.just(goalService.updateDeposit(principal.getName(), depositRequest, idGoal));
    }
}
