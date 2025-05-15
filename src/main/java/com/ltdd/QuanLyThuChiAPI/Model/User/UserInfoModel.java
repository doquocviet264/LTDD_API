package com.ltdd.QuanLyThuChiAPI.Model.User;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "user_info")
public class UserInfoModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", columnDefinition = "nvarchar(255)")
    private String firstname;

    @Column(name = "lastname", columnDefinition = "nvarchar(255)")
    private String lastname;

    @Column(name = "date")
    private String date;

    @Column(name = "avatar")
    private String avatar;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AccountModel accountModel;

    public UserInfoModel() {
    }

    public UserInfoModel(Long id, String firstname, String lastname, String date, String avatar, AccountModel accountModel) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.avatar = avatar;
        this.accountModel = accountModel;
    }
}
