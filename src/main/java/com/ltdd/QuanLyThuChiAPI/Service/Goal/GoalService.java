package com.ltdd.QuanLyThuChiAPI.Service.Goal;

import com.ltdd.QuanLyThuChiAPI.Interface.Goal.GoalInterface;
import com.ltdd.QuanLyThuChiAPI.Model.Goal.GoalModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import com.ltdd.QuanLyThuChiAPI.Repository.Goal.GoalRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.Notification.NotificationRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.AccountRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.UserInfoRepository;
import com.ltdd.QuanLyThuChiAPI.Request.Goal.DepositRequest;
import com.ltdd.QuanLyThuChiAPI.Request.Goal.GoalRequest;
import com.ltdd.QuanLyThuChiAPI.Request.Goal.UpdateGoalRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService implements GoalInterface {
    @Autowired
    GoalRepository goalRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public ApiResponse<Object> getAllGoals(String username)
    {
        List<GoalModel> listGoal = goalRepository.findAllByUserInfoModel_AccountModel_Username(username);
        if(listGoal.size() == 0)
        {
            return ApiResponse.builder().message("Không có mục tiêu").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách mục tiêu").status(200).data(listGoal).build();
    }

    @Override
    public ApiResponse<Object> addGoal(String username, GoalRequest goalRequest) throws ParseException {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Account không tồn tại!").status(101).build();
        }
        Optional<UserInfoModel> userInfoModel = Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
        if(!userInfoModel.isPresent())
        {
            return ApiResponse.builder().message("User không tồn tại!").status(101).build();
        }
        GoalModel goalCheck = goalRepository.findByName(goalRequest.getName());
        if(goalCheck != null)
        {
            return ApiResponse.builder().message("Mục tiêu đã tồn tại").status(101).build();
        }
        if(goalRequest.getBalance() >= goalRequest.getAmount())
        {
            return ApiResponse.builder().message("Số tiền bắt đầu mục tiêu không thể lớn hơn hoặc bằng số tiền mục tiêu!").status(101).build();
        }
        Date currentDate = new Date(System.currentTimeMillis());
        if(currentDate.after(goalRequest.getDeadline()))
        {
            return ApiResponse.builder().message("Ngày mục tiêu phải lớn hơn ngày hiện tại!").status(101).build();
        }
        GoalModel goalModel = GoalModel.builder().userInfoModel(userInfoModel.get())
                .name(goalRequest.getName())
                .balance(goalRequest.getBalance())
                .amount(goalRequest.getAmount())
                .deposit(0L)
                .deadline(goalRequest.getDeadline())
                .status(1L).build();
        goalRepository.save(goalModel);
        return ApiResponse.builder().message("Thêm mục tiêu thành công!").status(200).data(goalModel).build();
    }

    @Override
    public ApiResponse<Object> deleteGoal(String username, Long idGoal)
    {
        GoalModel goalModel = goalRepository.findGoalModelByUserInfoModel_AccountModel_UsernameAndId(username, idGoal);
        if(goalModel == null || goalModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy mục tiêu").status(101).build();
        }
        goalRepository.delete(goalModel);
        return ApiResponse.builder().message("Xóa mục tiêu thành công!").status(200).build();
    }

    @Override
    public ApiResponse<Object> updateGoal(String username, UpdateGoalRequest updateGoalRequest, Long idGoal) {
        GoalModel goalModel = goalRepository.findGoalModelByUserInfoModel_AccountModel_UsernameAndId(username, idGoal);
        if(goalModel == null || goalModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Mục tiêu không tồn tại!").status(101).build();
        }
        if(updateGoalRequest.getBalance() > updateGoalRequest.getAmount())
        {
            return ApiResponse.builder().message("Số tiền bắt đầu mục tiêu không thể lớn hơn hoặc bằng số tiền mục tiêu!").status(101).build();
        }

        Date currentDate = new Date(System.currentTimeMillis());
        if(currentDate.after(updateGoalRequest.getDeadline()))
        {
            return ApiResponse.builder().message("Ngày mục tiêu phải lớn hơn ngày hiện tại!").status(101).build();
        }
        goalModel.setName(updateGoalRequest.getName());
        goalModel.setBalance(updateGoalRequest.getBalance());
        goalModel.setAmount(updateGoalRequest.getAmount());
        goalModel.setDeadline(updateGoalRequest.getDeadline());

        goalRepository.save(goalModel);
        return ApiResponse.builder().message("Sửa mục tiêu thành công!").status(200).build();
    }

    @Override
    public ApiResponse<Object> updateDeposit(String username, DepositRequest depositRequest, Long idGoal)
    {
        GoalModel goalModel = goalRepository.findGoalModelByUserInfoModel_AccountModel_UsernameAndId(username, idGoal);
        if(goalModel == null || goalModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Mục tiêu không tồn tại!").status(101).build();
        }

        goalModel.setDeposit(depositRequest.getDeposit()+goalModel.getDeposit());

        goalRepository.save(goalModel);
        return ApiResponse.builder().message("Thêm tiền cho mục tiêu thành công!").status(200).build();
    }
}
