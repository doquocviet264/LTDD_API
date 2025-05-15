package com.ltdd.QuanLyThuChiAPI.Model.User;

import com.ltdd.QuanLyThuChiAPI.Model.Role.RoleModel;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "account")
public class AccountModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "username", unique = true)
    private String username;

    @NonNull
    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleModel roleModel;

    @Column(name = "isActivity")
    private boolean isActivity;


    public AccountModel() {

    }

    public AccountModel(Long id, @NonNull String username, @NonNull String password, String email, RoleModel roleModel, boolean isActivity) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleModel = roleModel;
        this.isActivity = isActivity;
    }
}
