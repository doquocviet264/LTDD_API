package com.ltdd.QuanLyThuChiAPI.Model.Card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "bank_cards")
@NoArgsConstructor
@AllArgsConstructor
public class CardModel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserInfoModel userInfoModel;
    
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "balance")
    private Long balance;


    @Column(name = "cardnumber")
    private String cardnumber;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @JsonIgnore
    private String created_at;

    @Column(name = "updated_at")
    @JsonIgnore
    private String updated_at;
}
