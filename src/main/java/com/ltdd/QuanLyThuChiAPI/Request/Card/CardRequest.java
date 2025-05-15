package com.ltdd.QuanLyThuChiAPI.Request.Card;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardRequest {
    private String name;
    private Long balance;
    private String cardnumber;
    private String description;
}
