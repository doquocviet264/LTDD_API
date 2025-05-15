package com.ltdd.QuanLyThuChiAPI.Service.Budget;

import com.ltdd.QuanLyThuChiAPI.Interface.Budget.BudgetInterface;
import com.ltdd.QuanLyThuChiAPI.Model.Budget.BudgetModel;
import com.ltdd.QuanLyThuChiAPI.Model.Category.CategoryModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import com.ltdd.QuanLyThuChiAPI.Repository.Budget.BudgetRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.Category.CategoryRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.AccountRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.UserInfoRepository;
import com.ltdd.QuanLyThuChiAPI.Request.Budget.BudgetRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService implements BudgetInterface {

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ApiResponse<Object> getBudgetById(String username, Long idBudget)
    {
        BudgetModel budgetModel = budgetRepository.findByUsernameAndId(username, idBudget);
        if(budgetModel == null)
            return ApiResponse.builder().message("Không tìm thấy ngân sách").status(101).build();

        return ApiResponse.builder().message("Chi tiết ngân sách").status(200).data(budgetModel).build();
    }

    @Override
    public ApiResponse<Object> getAll(String username)
    {
        List<BudgetModel> list = budgetRepository.findAllByUsername(username);
        if(list.size() == 0){
            return ApiResponse.builder().message("Chưa có ngân sách").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách ngân sách").status(200).data(list).build();
    }

    // lấy thông tin 1 ngân sách theo danh mục chi tiêu
    @Override
    public ApiResponse<Object> getBudgetByCategory(String username, Long idCategory)
    {
        BudgetModel budgetModel = budgetRepository.findByUsernameAndId(username, idCategory);
        if(budgetModel == null)
            return ApiResponse.builder().message("Không tìm thấy ngân sách!").status(101).build();

        return ApiResponse.builder().message("Thông tin ngân sách").status(200).data(budgetModel).build();
    }


    @Override
    public ApiResponse<Object> addBudget(String username, Long idCategory, BudgetRequest budgetRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
            return ApiResponse.builder().message("Account không tồn tại!").status(101).build();

        Optional<UserInfoModel> userInfoModel = Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel_Username(username));
        if(!userInfoModel.isPresent())
            return ApiResponse.builder().message("User không tồn tại!").build();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        BudgetModel budgetModel = budgetRepository.findBudgetByUsernameAndIdcategoryAndFromdateAndTodate(username, idCategory, fromDate, toDate);
        if(budgetModel != null)
        {
            return ApiResponse.builder().message("Ngân sách cho danh mục chi tiêu trong tháng đã tồn tại").status(101).build();
        }

        CategoryModel categoryModel = categoryRepository.findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(username, idCategory);
        BudgetModel budgetModel1 = BudgetModel.builder().userInfoModel(userInfoModel.get())
                .categoryModel(categoryModel).amount(budgetRequest.getAmount())
                .fromdate(fromDate).todate(toDate).description(budgetRequest.getDescription()).build();

        budgetRepository.save(budgetModel1);
        return ApiResponse.builder().message("Tạo ngân sách thành công!").status(200).data(budgetModel1).build();
    }

    @Override
    public ApiResponse<Object> updateBudget(String username, Long idBudget, BudgetRequest budgetRequest)
    {
        BudgetModel budgetModel = budgetRepository.findByUsernameAndId(username, idBudget);
        if(budgetModel == null || budgetModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Khong tìm thấy tên ngân sách").status(101).build();
        }
        else
        {
            if (budgetRequest.getAmount() != null && !budgetRequest.getAmount().toString().isEmpty()) {
                budgetModel.setAmount(budgetRequest.getAmount());
            }
            if (budgetRequest.getDescription() != null && !budgetRequest.getDescription().isEmpty()) {
                budgetModel.setDescription(budgetRequest.getDescription());
            }
        }
        budgetRepository.save(budgetModel);
        return ApiResponse.builder().message("Sửa ngân sách thành công!").status(200).data(budgetModel).build();
    }

    @Override
    public ApiResponse<Object> deleteBudget(String username, Long idBudget)
    {
        BudgetModel budgetModel = budgetRepository.findByUsernameAndId(username, idBudget);
        if(budgetModel == null || budgetModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy ngân sách").status(101).build();
        }
        budgetRepository.delete(budgetModel);
        return ApiResponse.builder().message("Xóa ngân sách thành công").status(200).build();
    }

    // lấy tất cả ngân sách trong 1 tháng
    @Override
    public ApiResponse<Object> getAllInMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        List<BudgetModel> list = budgetRepository.getAllInMonth(username, fromDate, toDate);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Chưa có ngân sách trong tháng").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách ngân sách trong tháng").status(200).data(list).build();
    }

}
