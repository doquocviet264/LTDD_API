package com.ltdd.QuanLyThuChiAPI.Model.Goal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Builder
@Data
@Entity
@Table(name = "goals")
@NoArgsConstructor
@AllArgsConstructor
public class GoalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserInfoModel userInfoModel;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "balance")
    private Long balance;

    @NonNull
    @Column(name = "amount")
    private Long amount;

    @Column(name = "deposit")
    private Long deposit;

    @NonNull
    @Column(name = "deadline")
    private Date deadline;

    // status = 1: chưa hoàn thành, = 2 đã hoan thành, = 3 quá hạn
    @NonNull
    @Column(name = "status")
    private Long status;
}
