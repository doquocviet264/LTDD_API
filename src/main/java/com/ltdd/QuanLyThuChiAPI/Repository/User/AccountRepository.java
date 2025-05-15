package com.ltdd.QuanLyThuChiAPI.Repository.User;

import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long>{
        @Query("SELECT u FROM AccountModel u WHERE u.username=:userName")
        AccountModel findAccountModelByUsername(@Param("userName") String userName);

        AccountModel findAccountModelByEmail(String email);

        AccountModel findAccountModelById(Long id);
        boolean existsByEmail( String email);
}
