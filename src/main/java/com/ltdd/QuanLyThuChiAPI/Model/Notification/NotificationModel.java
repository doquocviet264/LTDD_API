package com.ltdd.QuanLyThuChiAPI.Model.Notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserInfoModel userInfoModel;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean is_read;

    @Column(name = "created_at")
    private Date created_at;

}
