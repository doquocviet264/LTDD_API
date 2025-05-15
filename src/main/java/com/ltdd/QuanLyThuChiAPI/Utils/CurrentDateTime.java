package com.ltdd.QuanLyThuChiAPI.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentDateTime {
    public static String getCurrentDateTime()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return now.format(formatter);
    }
}
