package com.ltdd.QuanLyThuChiAPI.Repository.User;

import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoModel, Long> {

    @Query("SELECT u FROM UserInfoModel u WHERE u.accountModel=:account")
    UserInfoModel findUserInfoModelByAccountModel(@Param("account")AccountModel accountModel);

    @Query("SELECT u FROM UserInfoModel u WHERE u.accountModel.username = :username")
    UserInfoModel findUserInfoModelByAccountModel_Username(@Param("username") String username);

    Iterable<UserInfoModel> findAllByAccountModel_RoleModel_Name(String roleName);
}
