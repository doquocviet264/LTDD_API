package com.ltdd.QuanLyThuChiAPI.Request.Transaction;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class TransactionRequest {
    private String name;

    private Long amount;

    private String location;

    private Date transactiondate;

    private String description;
}
