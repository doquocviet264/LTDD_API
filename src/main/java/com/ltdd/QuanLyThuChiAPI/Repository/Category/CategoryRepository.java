package com.ltdd.QuanLyThuChiAPI.Repository.Category;

import com.ltdd.QuanLyThuChiAPI.Model.Category.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {

    @Query("SELECT c FROM CategoryModel c JOIN c.userInfoModel u JOIN u.accountModel a WHERE a.username = :username")
    Iterable<CategoryModel> findAllByUserInfoModel_AccountModel_Username(@Param("username") String username);

    @Query("SELECT c FROM CategoryModel c JOIN c.userInfoModel u JOIN u.accountModel a WHERE a.username = :username AND c.type = :type")
    List<CategoryModel> findAllByUserInfoModel_AccountModel_UsernameaAndAndType(@Param("username") String username,@Param("type") Long type);

    @Query("SELECT c FROM CategoryModel c JOIN c.userInfoModel u JOIN u.accountModel a WHERE a.username = :username AND c.id = :id")
    CategoryModel findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(@Param("username") String username, @Param("id") Long id);

    @Query("SELECT COUNT(t) FROM TransactionModel t WHERE t.userInfoModel.accountModel.username = :username AND t.categoryModel.id = :categoryId")
    Long countTransactionsByCategoryId(@Param("username") String username, @Param("categoryId") Long categoryId);

    @Query("SELECT c FROM CategoryModel c WHERE c.userInfoModel.accountModel.username = :username AND c.name = :name AND c.type = :type")
    CategoryModel findCategoryModelByNameAndType(@Param("username") String username, @Param("name") String name, @Param("type") Long type);
}
