package com.ltdd.QuanLyThuChiAPI.Repository.User;

import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {
    ForgotPassword findByVerifyCode(String verifyCode);

    ForgotPassword findForgotPasswordByAccountModel(AccountModel accountModel);
}
