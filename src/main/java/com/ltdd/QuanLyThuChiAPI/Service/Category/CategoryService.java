package com.ltdd.QuanLyThuChiAPI.Service.Category;

import com.ltdd.QuanLyThuChiAPI.Interface.Category.CategoryInterface;
import com.ltdd.QuanLyThuChiAPI.Model.Category.CategoryModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import com.ltdd.QuanLyThuChiAPI.Repository.Category.CategoryRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.AccountRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.UserInfoRepository;
import com.ltdd.QuanLyThuChiAPI.Request.Category.CategoryRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Utils.CurrentDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CategoryService implements CategoryInterface {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public ApiResponse<Object> addCategory(String username, CategoryRequest categoryRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Account không tồn tại!").status(200).build();
        }
        Optional<UserInfoModel> userInfoModel = Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
        if (!userInfoModel.isPresent()) {
            return ApiResponse.builder().message("User không tồn tại!").status(200).build();
        }
        CategoryModel category = categoryRepository.findCategoryModelByNameAndType(username, categoryRequest.getName(), categoryRequest.getType());
        if(category != null)
        {
            return ApiResponse.builder().message("Đã tồn tại danh mục với thể loại chi tiêu").status(101).build();
        }
        CategoryModel categoryModel = CategoryModel.builder().userInfoModel(userInfoModel.get()).description(categoryRequest.getDescription())
                        .name(categoryRequest.getName())
                        .color(categoryRequest.getColor())
                        .type(categoryRequest.getType())
                        .created_at(CurrentDateTime.getCurrentDateTime())
                        .updated_at(CurrentDateTime.getCurrentDateTime()).build();
        categoryRepository.save(categoryModel);
        return ApiResponse.builder().status(200).message("Tạo danh mục chi tiêu thành công!").data(categoryModel).build();
    }

    @Override
    public ApiResponse<Object> updateCategory(String username, CategoryRequest categoryRequest, Long idCategory)
    {
        CategoryModel categoryModel = categoryRepository.findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(username, idCategory);
        if(categoryModel == null || categoryModel.getId() <= 0)
        {
            return ApiResponse.builder().status(101).message("Không thể cập nhật danh mục chi tiêu").data(null).build();
        }
        CategoryModel category = categoryRepository.findCategoryModelByNameAndType(username, categoryRequest.getName(), categoryRequest.getType());
        if(category != null)
        {
            return ApiResponse.builder().message("Đã tồn tại danh mục với thể loại chi tiêu").status(101).build();
        }
        else {
            if (categoryRequest.getName() != null && !categoryRequest.getName().isEmpty()) {
                categoryModel.setName(categoryRequest.getName());
            }
            if (categoryRequest.getDescription() != null && !categoryRequest.getDescription().isEmpty()) {
                categoryModel.setDescription(categoryRequest.getDescription());
            }
            if (categoryRequest.getColor() != null && !categoryRequest.getColor().isEmpty()) {
                categoryModel.setColor(categoryRequest.getColor());
            }
            if (categoryRequest.getType() != null && categoryRequest.getType() > 0) {
                categoryModel.setType(categoryRequest.getType());
            }
        }
        categoryModel.setUpdated_at(CurrentDateTime.getCurrentDateTime());
        categoryRepository.save(categoryModel);
        return ApiResponse.builder().status(200).message("Cập nhật danh mục chi tiêu thành công").data(null).build();
    }

    @Override
    public ApiResponse<Object> deleteCategory(String username, Long categoryId)
    {
        CategoryModel categoryModel = categoryRepository.findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(username, categoryId);
        if(categoryModel == null || categoryModel.getId() < 0)
        {
            return ApiResponse.builder().message("Không tìm thấy danh mục!").status(101).build();
        }
        Long transactionCount = categoryRepository.countTransactionsByCategoryId(username, categoryId);
        if(transactionCount > 0)
        {
            return ApiResponse.builder().message("Không thể xóa danh mục vì có giao dịch liên quan!").status(101).build();
        }
        categoryRepository.delete(categoryModel);
        return ApiResponse.builder().status(200).message("Xóa danh mục thành công").build();
    }

    //lấy thông tin chi tiết về một danh mục của user
    @Override
    public CategoryModel getCategory(String username, Long categoryId)
    {
        return categoryRepository.findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(username, categoryId);
    }

    // lấy ra các danh mục của user
    @Override
    public Iterable<CategoryModel> getAllCategory(String username)
    {
        return categoryRepository.findAllByUserInfoModel_AccountModel_Username(username);
    }

    // lấy ra các danh mục thu về
    @Override
    public List<CategoryModel> imcomeCategory(String username)
    {
        return categoryRepository.findAllByUserInfoModel_AccountModel_UsernameaAndAndType(username, 1L);
    }

    // lấy ra các danh mục chi tiêu
    @Override
    public List<CategoryModel> expenseCategory(String username)
    {

        return categoryRepository.findAllByUserInfoModel_AccountModel_UsernameaAndAndType(username, 2L);
    }


}
