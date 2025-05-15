package com.ltdd.QuanLyThuChiAPI.Repository.Goal;

import com.ltdd.QuanLyThuChiAPI.Model.Goal.GoalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<GoalModel, Long> {
    @Query("SELECT c FROM GoalModel c JOIN c.userInfoModel u JOIN u.accountModel a WHERE a.username = :username")
    List<GoalModel> findAllByUserInfoModel_AccountModel_Username(@Param("username") String username);

    @Query("SELECT c FROM GoalModel c JOIN c.userInfoModel u JOIN u.accountModel a WHERE a.username = :username AND c.id = :id")
    GoalModel findGoalModelByUserInfoModel_AccountModel_UsernameAndId(@Param("username") String username, @Param("id") Long id);

    @Query("SELECT c FROM GoalModel c WHERE c.name = :name AND (c.status = 1L OR c.status = 3L)")
    GoalModel findByName(@Param("name") String name);
}
