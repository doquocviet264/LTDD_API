package com.ltdd.QuanLyThuChiAPI.Repository.Budget;

import com.ltdd.QuanLyThuChiAPI.Model.Budget.BudgetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetModel, Long> {

    @Query("SELECT b FROM BudgetModel b WHERE b.userInfoModel.accountModel.username = :username")
    List<BudgetModel> findAllByUsername(@Param("username") String username);

    @Query("SELECT b FROM BudgetModel b WHERE b.userInfoModel.accountModel.username = :username AND b.categoryModel.id = :idCategory")
    BudgetModel findBudgetByIdCategory(@Param("username") String username, @Param("idCategory") Long idCategory);

    @Query("SELECT b FROM BudgetModel b WHERE b.userInfoModel.accountModel.username = :username AND b.categoryModel.id = :idCategory AND fromdate = :fromdate AND todate = :todate")
    BudgetModel findBudgetByUsernameAndIdcategoryAndFromdateAndTodate(@Param("username") String username, @Param("idCategory") Long idCategory, @Param("fromdate") Date fromdate, @Param("todate") Date todate);

    // lấy danh sách ngân sách theo tháng
    @Query("SELECT b FROM BudgetModel b WHERE b.userInfoModel.accountModel.username = :username AND fromdate = :fromdate AND todate = :todate")
    List<BudgetModel> getAllInMonth(@Param("username") String username, @Param("fromdate") Date fromDate, @Param("todate") Date toDate);

    @Query("SELECT b FROM BudgetModel b WHERE b.userInfoModel.accountModel.username = :username AND b.id = :id")
    BudgetModel findByUsernameAndId(@Param("username") String username, @Param("id") Long id);



}
