package com.ltdd.QuanLyThuChiAPI.Service.User;

import com.ltdd.QuanLyThuChiAPI.Interface.User.UserInterface;
import com.ltdd.QuanLyThuChiAPI.Model.Role.RoleModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.ForgotPassword;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import com.ltdd.QuanLyThuChiAPI.Repository.Role.RoleRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.AccountRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.ForgotPasswordRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.UserInfoRepository;
import com.ltdd.QuanLyThuChiAPI.Request.User.*;
import com.ltdd.QuanLyThuChiAPI.Request.User.*;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Response.Login.LoginResponse;
import com.ltdd.QuanLyThuChiAPI.Response.User.PasswordResponse;
import com.ltdd.QuanLyThuChiAPI.Response.User.UserResponse;
import com.ltdd.QuanLyThuChiAPI.Security.JWT.JwtProvider;
import com.ltdd.QuanLyThuChiAPI.Utils.Constants;
import com.ltdd.QuanLyThuChiAPI.Utils.CurrentDateTime;
import com.ltdd.QuanLyThuChiAPI.Utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Transactional
@Service
public class UserService implements UserInterface{

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Transactional
    @Override
    public UserResponse registerUser(UserRequest userRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(userRequest.getUserName());
        if(accountModel != null && accountModel.getId() >= 0){
            return UserResponse.builder().status(false).message("Tài khoản đã tồn tại. Vui lòng nhập lại!").build();
        }
        if(accountRepository.existsByEmail(userRequest.getEmail()))
        {
            return UserResponse.builder().status(false).message("Email đã tồn tại. Vui lòng nhập lại!").build();
        }
        RoleModel roleModel = roleRepository.findByName(userRequest.getRoleName());
        if(roleModel == null)
        {
            return UserResponse.builder().status(false).message("Role không tồn tại. Vui lòng kiểm tra lại!").build();
        }

        String saltedPassword = userRequest.getPassWord() + Constants.SALT_DEFAULT;
        String hashedPassword = passwordEncoder.encode(saltedPassword);
        accountModel = AccountModel.builder().username(userRequest.getUserName()).isActivity(true).roleModel(roleModel).email(userRequest.getEmail()).password(hashedPassword).build();
        UserInfoModel userInfoModel =UserInfoModel.builder().firstname(userRequest.getFirstName())
                                    .lastname(userRequest.getLastName())
                                    .date(CurrentDateTime.getCurrentDateTime())
                                    .accountModel(accountModel)
                                    .avatar(userRequest.getAvatar())
                                    .build();
        accountRepository.save(accountModel);
        userInfoRepository.save(userInfoModel);

        return UserResponse.builder().status(true).message("Tạo tài khoản thành công!").userInfoModel(userInfoModel).roleModel(roleModel).build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(loginRequest.getUsername());
        if(accountModel == null || accountModel.getId() == null)
        {
            return LoginResponse.builder().message("Tài khoản không hợp lệ. Vui lòng thử lại!").status(false).build();
        }
        if(accountModel.isActivity() == false)
        {
            return LoginResponse.builder().message("Tài khoản đã bị khóa! Hãy liên hệ với chúng tôi!").status(false).build();
        }
        if(passwordEncoder.matches(loginRequest.getPassword() + Constants.SALT_DEFAULT, accountModel.getPassword()))
        {
            return buildToken(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
        }else{
            return LoginResponse.builder().message("Tài khoản mật khẩu không hợp lệ. Vui lòng thử lại").status(false).build();
    }
    }

    private LoginResponse buildToken(UserInfoModel userInfoModel)
    {
        return LoginResponse.builder().userInfoModel(userInfoModel).accessToken(jwtProvider.createToken(userInfoModel)).status(true).build();
    }

    @Override
    public UserResponse getProfileUser(String username)
    {
        UserInfoModel userInfoModel = userInfoRepository.findUserInfoModelByAccountModel_Username(username);
        return UserResponse.builder().status(true)
                .userInfoModel(userInfoModel)
                .roleModel(userInfoModel.getAccountModel().getRoleModel())
                .build();
    }
    @Override
    public ApiResponse<Object> getAllUser()
    {
        Iterable<UserInfoModel> listUser = userInfoRepository.findAllByAccountModel_RoleModel_Name("USER");
        return ApiResponse.builder().message("Danh sách người dùng").status(200).data(listUser).build();
    }
    @Override
    public ApiResponse<Object> deleteAccountUser(Long id)
    {
        AccountModel accountModel = accountRepository.findAccountModelById(id);
        if(accountModel.isActivity() == false)
        {
            return ApiResponse.builder().message("Tài khoản hiện đang bị khóa").status(101).build();
        }
        accountModel.setActivity(false);
        accountRepository.save(accountModel);
        return ApiResponse.builder().status(200).message("Tài khoản đã bị vô hiệu hóa!").data(accountModel).build();
    }

    @Override
    public ApiResponse<Object> openAccountUser(Long id)
    {
        AccountModel accountModel = accountRepository.findAccountModelById(id);
        if(accountModel.isActivity() == true)
        {
            return ApiResponse.builder().message("Tài khoản đã đang hoạt động").status(101).build();
        }
        accountModel.setActivity(true);
        accountRepository.save(accountModel);
        return ApiResponse.builder().status(200).message("Mở khóa tài khoản thành công").data(accountModel).build();
    }
    @Override
    public ApiResponse<Object> updateProfile(String username, UpdateProfileRequest updateProfileRequest)
    {
        UserInfoModel userInfoModel = userInfoRepository.findUserInfoModelByAccountModel_Username(username);
        if (userInfoModel == null || userInfoModel.getId() == null || userInfoModel.getId() == 0) {
            return ApiResponse.builder().message("Không tìm thấy thông tin người dùng").status(400).build();
        }
        if (updateProfileRequest.getFirstname() != null && !updateProfileRequest.getFirstname().isEmpty()) {
            userInfoModel.setFirstname(updateProfileRequest.getFirstname());
        }
        if (updateProfileRequest.getLastname() != null && !updateProfileRequest.getLastname().isEmpty()) {
            userInfoModel.setLastname(updateProfileRequest.getLastname());
        }
        if (updateProfileRequest.getAvatar() != null && !updateProfileRequest.getAvatar().isEmpty()) {
            userInfoModel.setAvatar(updateProfileRequest.getAvatar());
        }
        return ApiResponse.builder().message("Cập nhật thông tin thành công").status(200).data(userInfoModel).build();
    }

    @Override
    public PasswordResponse forgotPass(ForgotPasswordRequest forgotPasswordRequest)
    {
        boolean result =false;
        String message = "";
        int errorCode =0;

        AccountModel accountModel = accountRepository.findAccountModelByEmail(forgotPasswordRequest.getEmail());
        if(accountModel != null && accountModel.getId() > 0)
        {
            result = true;
            String verifyCode = RandomUtils.getAlphaNumericString(20);
            ForgotPassword forgotPassword = forgotPasswordRepository.findForgotPasswordByAccountModel(accountModel);
            if(forgotPassword == null || forgotPassword.getId() == null)
            {
                forgotPasswordRepository.save(ForgotPassword.builder()
                        .accountModel(accountModel)
                        .verifyCode(verifyCode)
                        .useCode(true)
                        .expiryDate(System.currentTimeMillis()+Duration.ofHours(8).toMillis()).build());
            }
            else{
                forgotPassword.setVerifyCode(verifyCode);
                forgotPassword.setUseCode(true);
                forgotPassword.setExpiryDate(System.currentTimeMillis()+Duration.ofHours(8).toMillis());
                forgotPasswordRepository.save(forgotPassword);
            }
            emailSenderService.sendMail(accountModel, verifyCode);
            message = "Kiểm tra mail để lấy lại mật khẩu!";
        }
        else{
            errorCode = 21;
            message = "Email không tồn tại trong hệ thống! Vui lòng thử lại";
        }
        return PasswordResponse.builder().status(result).message(message).errorCode(errorCode).build();
    }


    @Override
    public PasswordResponse verifyCode(String codeRequest){
        ForgotPassword forgotPassword = forgotPasswordRepository.findByVerifyCode(codeRequest);
        if(forgotPassword == null || forgotPassword.getId() == null)
        {
            return PasswordResponse.builder().message("Verify code không tồn tại!").status(false).build();
        }
        else if(!forgotPassword.isUseCode() || forgotPassword.getExpiryDate() < System.currentTimeMillis())
        {
            return PasswordResponse.builder().message("Verify code đã được sử dụng hoặc hết hạn!").status(false).build();
        }
        return PasswordResponse.builder().message("Mã xác minh hợp lệ!").status(true).build();
    }

    @Override
    public PasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest)
    {
        boolean result = false;
        String message = "";
        int errorCode = 0;

        ForgotPassword forgotPassword = forgotPasswordRepository.findByVerifyCode(resetPasswordRequest.getVerifyCode());
        if(forgotPassword != null && forgotPassword.getId() >= 0)
        {
            if(!forgotPassword.isUseCode())
            {
                return PasswordResponse.builder().message("Vui lòng gửi la yêu cầu mới. Liên kết đã được sử dụng!").status(false).build();
            }
            AccountModel accountModel =forgotPassword.getAccountModel();
            if(accountModel != null && accountModel.getId() > 0)
            {
                accountModel.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword() + Constants.SALT_DEFAULT));
                accountRepository.save(accountModel);
                forgotPassword.setUseCode(false);
                forgotPasswordRepository.save(forgotPassword);
                result = true;
                message = "Bạn có thể sử dụng mật khẩu mới để đăng nhập vào ệ thống!";
            }
            else {
                message = "Không thể tìm thấy tài khoản với email của bạn!";
                errorCode = 31;
            }
        }
        else
        {
            message = "Mã đặt lại không hợp lệ! Vui lòng kiểm tra lại";
            errorCode = 32;
        }
        return PasswordResponse.builder().status(result).message(message).errorCode(errorCode).build();
    }
}
