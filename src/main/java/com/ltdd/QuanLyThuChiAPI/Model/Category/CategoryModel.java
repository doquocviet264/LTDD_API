package com.ltdd.QuanLyThuChiAPI.Model.Category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserInfoModel userInfoModel;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "type") // loai = 1: thu, loai = 2: chi
    private Long type;

    @Column(name = "created_at")
    @JsonIgnore
    private String created_at;

    @Column(name = "updated_at")
    @JsonIgnore
    private String updated_at;
}
