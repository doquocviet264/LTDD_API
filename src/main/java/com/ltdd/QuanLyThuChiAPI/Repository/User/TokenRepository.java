//package com.ltdd.QuanLyThuChiAPI.Repository.User;
//
//import com.ltdd.QuanLyThuChiAPI.Model.User.TokenModel;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface TokenRepository extends JpaRepository<TokenModel, Long> {
//
//    @Query("SELECT t FROM TokenModel t WHERE t.user_id=:user_id")
//    TokenModel findByUser_id(@Param("user_id") String user_id);
//
//    @Query("SELECT t FROM TokenModel t WHERE t.token_id=:token_id")
//    TokenModel findByToken_id(@Param("token_id") String token_id);
//
//    @Modifying
//    @Query("DELETE FROM TokenModel t WHERE t.token_id=:token_id")
//    TokenModel deleteTokenModelByToken_id(@Param("token_id") String token_id);
//}
