package com.ltdd.QuanLyThuChiAPI.Request.Goal;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class UpdateGoalRequest {
    private String name;

    private Long balance;

    private Long amount;

    private Date deadline;
}
