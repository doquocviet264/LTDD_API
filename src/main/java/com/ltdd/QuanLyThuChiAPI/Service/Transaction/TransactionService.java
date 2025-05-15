package com.ltdd.QuanLyThuChiAPI.Service.Transaction;

import com.ltdd.QuanLyThuChiAPI.Interface.Transaction.TransactionInterface;
import com.ltdd.QuanLyThuChiAPI.Model.Card.CardModel;
import com.ltdd.QuanLyThuChiAPI.Model.Category.CategoryModel;
import com.ltdd.QuanLyThuChiAPI.Model.Transaction.TransactionModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import com.ltdd.QuanLyThuChiAPI.Repository.Card.CardRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.Category.CategoryRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.Transaction.TransactionRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.AccountRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.UserInfoRepository;
import com.ltdd.QuanLyThuChiAPI.Request.Transaction.TransactionRequest;
import com.ltdd.QuanLyThuChiAPI.Request.Transaction.UpdateTransactionRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements TransactionInterface {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ApiResponse<Object> incomeTransaction(String username)
    {
        List<TransactionModel> list =  transactionRepository.findAllByUserInfoModel_AccountModel_UsernameaAndAndType(username, 1L);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Không có giao dịch thu").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách giao dịch thu tiền").status(200).data(list).build();
    }

    @Override
    public ApiResponse<Object> expenseTransaction(String username)
    {
        List<TransactionModel> list = transactionRepository.findAllByUserInfoModel_AccountModel_UsernameaAndAndType(username, 2L);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Không có giao dịch chi tiêu").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách giao dịch chi tiêu").status(200).data(list).build();
    }

    @Override
    public ApiResponse<Object> getAll(String username)
    {
        List<TransactionModel> list = transactionRepository.findAllByUsername(username);
        return ApiResponse.builder().message("Danh sách giao dịch").status(200).data(list).build();
    }

    // thêm giao dịch thu nhập
    @Override
    public ApiResponse<Object> addTransactionIncome(String username, Long idCategory,Long idCard, TransactionRequest transactionRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
            return ApiResponse.builder().message("Account không tồn tại!").status(101).build();
        Optional<UserInfoModel> userInfoModel = Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel_Username(username));
        if(!userInfoModel.isPresent())
            return ApiResponse.builder().message("User không tồn tại!").build();
        CategoryModel categoryModel = categoryRepository.findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(username, idCategory);
        if(categoryModel.getType() != 1L)
        {
            return ApiResponse.builder().message("Thể loại của giao dịch và danh mục giao dịch k trùng khớp!").status(101).build();
        }
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        cardModel.setBalance(cardModel.getBalance() + transactionRequest.getAmount());
        cardRepository.save(cardModel);

        TransactionModel transactionModel = TransactionModel.builder()
                            .userInfoModel(userInfoModel.get())
                            .categoryModel(categoryModel)
                            .cardModel(cardModel)
                            .name(transactionRequest.getName())
                            .amount(transactionRequest.getAmount())
                            .location(transactionRequest.getLocation())
                            .transactiondate(transactionRequest.getTransactiondate())
                            .type(1L)
                            .description(transactionRequest.getDescription()).build();
        transactionRepository.save(transactionModel);
        return ApiResponse.builder().message("Tạo giao dịch thành công!").status(200).data(transactionModel).build();
    }

    // thêm giao dịch chi tiêu
    @Override
    public ApiResponse<Object> addTransactionExpense(String username, Long idCategory,Long idCard, TransactionRequest transactionRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
            return ApiResponse.builder().message("Account không tồn tại!").status(101).build();
        Optional<UserInfoModel> userInfoModel = Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel_Username(username));
        if(!userInfoModel.isPresent())
            return ApiResponse.builder().message("User không tồn tại!").build();
        CategoryModel categoryModel = categoryRepository.findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(username, idCategory);
        if(categoryModel.getType() != 2L)
        {
            return ApiResponse.builder().message("Thể loại của giao dịch và danh mục giao dịch k trùng khớp!").status(101).build();
        }
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        if(transactionRequest.getAmount() > cardModel.getBalance()) {
            return ApiResponse.builder().message("Số tiền trong thẻ không đủ để thực hiện giao dịch").status(100).build();
        }
        else
        {
            cardModel.setBalance(cardModel.getBalance() - transactionRequest.getAmount());
            cardRepository.save(cardModel);
        }

        TransactionModel transactionModel = TransactionModel.builder()
                .userInfoModel(userInfoModel.get())
                .categoryModel(categoryModel)
                .cardModel(cardModel)
                .name(transactionRequest.getName())
                .amount(transactionRequest.getAmount())
                .location(transactionRequest.getLocation())
                .transactiondate(transactionRequest.getTransactiondate())
                .type(2L)
                .description(transactionRequest.getDescription()).build();
        transactionRepository.save(transactionModel);
        return ApiResponse.builder().message("Tạo giao dịch thành công!").status(200).data(transactionModel).build();
    }


    @Override
    public ApiResponse<Object> updateTransaction(String username,Long idCategory,Long idTransaction, UpdateTransactionRequest updateTransactionRequest) {
        TransactionModel transactionModel = transactionRepository.findTransactionModelByUserInfoModel_AccountModel_UsernameAndId(username, idTransaction);
        if(transactionModel == null || transactionModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không thể sửa giao dịch!").status(101).build();
        }
        CategoryModel newCategory = categoryRepository.findCategoryModelByUserInfoModel_AccountModel_UsernameAndId(username,idCategory);
        if(newCategory.getType() != transactionModel.getType())
        {
            return ApiResponse.builder().message("Thể loại của danh mục không phù hợp với thể loại của giao dịch!").status(101).build();
        }

        transactionModel.setCategoryModel(newCategory);
        transactionModel.setName(updateTransactionRequest.getName());
        CardModel cardModel = transactionModel.getCardModel();
        if(transactionModel.getType() == 2)
        {
            if(transactionModel.getAmount() == updateTransactionRequest.getAmount()) {
                transactionModel.setAmount(updateTransactionRequest.getAmount());
            }
            else if(updateTransactionRequest.getAmount() > transactionModel.getAmount())
            {
                if((updateTransactionRequest.getAmount() - transactionModel.getAmount()) > transactionModel.getCardModel().getBalance())
                {
                    return ApiResponse.builder().message("Số tiền còn lại trong thẻ không đủ để chi trả cho giao dịch").status(100).build();
                }
                else{
                    cardModel.setBalance(cardModel.getBalance() - (updateTransactionRequest.getAmount() - transactionModel.getAmount()));
                    transactionModel.setAmount(updateTransactionRequest.getAmount());
                    cardRepository.save(cardModel);
                }
            }
            else
            {
                cardModel.setBalance(cardModel.getBalance() + (transactionModel.getAmount() - updateTransactionRequest.getAmount()));
                transactionModel.setAmount(updateTransactionRequest.getAmount());
                cardRepository.save(cardModel);
            }
        }
        else
        {
            if(transactionModel.getAmount() == updateTransactionRequest.getAmount()) {
                transactionModel.setAmount(updateTransactionRequest.getAmount());
            }
            else if(updateTransactionRequest.getAmount() > transactionModel.getAmount())
            {
                cardModel.setBalance(cardModel.getBalance() + (updateTransactionRequest.getAmount() - transactionModel.getAmount()));
                transactionModel.setAmount(updateTransactionRequest.getAmount());
                cardRepository.save(cardModel);
            }
            else
            {
                cardModel.setBalance(cardModel.getBalance() - (transactionModel.getAmount() - updateTransactionRequest.getAmount()));
                transactionModel.setAmount(updateTransactionRequest.getAmount());
                cardRepository.save(cardModel);
            }
        }
        transactionModel.setLocation(updateTransactionRequest.getLocation());
        transactionModel.setTransactiondate(updateTransactionRequest.getTransactiondate());
        transactionModel.setDescription(updateTransactionRequest.getDescription());
        transactionRepository.save(transactionModel);
        return ApiResponse.builder().message("Sửa giao dịch thành công").status(200).build();
    }

    @Override
    public ApiResponse<Object> deleteTransaction(String username, Long idTransaction)
    {
        TransactionModel transactionModel = transactionRepository.findTransactionModelByUserInfoModel_AccountModel_UsernameAndId(username, idTransaction);
        if(transactionModel == null || transactionModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy giao dịch").build();
        }
        transactionRepository.delete(transactionModel);
        return ApiResponse.builder().message("Xóa giao dịch thành công!").status(200).build();
    }

    // lấy danh sách các giao dịch trong ngày hôm nay
    @Override
    public ApiResponse<Object> getAllTransactionInDayCurrent(String username){
        Date current = new Date(System.currentTimeMillis());
        List<TransactionModel> list = transactionRepository.findAllByDateAndUsername(username, current);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Không có giao dịch trong ngày").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách giao dịch trong ngày").status(200).data(list).build();
    }

    // lấy danh sách giao dịch thu nhập trong ngày hiện tại
    @Override
    public ApiResponse<Object> getAllIncomeInDayCurrent(String username)
    {
        Date current = new Date(System.currentTimeMillis());
        List<TransactionModel> list = transactionRepository.findAllByUsernameAndDateAndType(username, current, 1L);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Chưa có giao dịch thu nhâp trong ngày").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách giao dịch thu nhập trong ngày").status(200).data(list).build();
    }

    // lấy danh sách giao dịch chi tiêu trong ngày hiện tại
    @Override
    public ApiResponse<Object> getAllExpenseInDayCurrent(String username)
    {
        Date current = new Date(System.currentTimeMillis());
        List<TransactionModel> list = transactionRepository.findAllByUsernameAndDateAndType(username, current, 2L);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Chưa có giao dịch chi tiêu trong ngày").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách giao dịch chi tiêu trong ngày").status(200).data(list).build();
    }

    // lấy tổng tiên thu về trong hôm nay
    @Override
    public ApiResponse<Object> getTotalIncomeToday(String username)
    {
        Date current = new Date(System.currentTimeMillis());
        Long total = transactionRepository.getTotalIncomeByDateAndUsername(username, current);
        if(total == null)
        {
            return ApiResponse.builder().message("Không có giao dịch thu về trong ngày").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền thu trong ngày").status(200).data(total).build();
    }

    // lấy tổng tiền chi tiêu trong hôm nay
    @Override
    public ApiResponse<Object> getTotalExpenseToday(String username)
    {
        Date current = new Date(System.currentTimeMillis());
        Long total = transactionRepository.getTotalExpenseByDateAndUsername(username, current);
        if(total == null)
        {
            return ApiResponse.builder().message("Không có giao dịch chi tiêu trong ngày").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền chi tiêu trong ngày").status(200).data(total).build();
    }

    // lấy danh sách các giao dịch trong tuần
    @Override
    public ApiResponse<Object> getAllInWeek(String usename)
    {
        // lấy ngày bắt đầu tuần
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+1);
        Date starWeek = new Date(cal.getTimeInMillis());

        // lấy ngày kết thúc tuần
        cal.add(Calendar.DAY_OF_MONTH, 6);
        Date endWeek = new Date(cal.getTimeInMillis());

        List<TransactionModel> list =  transactionRepository.findAllByUserInfoModel_AccountModel_UsernameAndTransactiondateBetween(usename, starWeek, endWeek);

        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Không có giao dịch trong tuần").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách các giao dịch trong tuần").status(200).data(list).build();
    }

    // lấy tổng tiền thu về của các giao dịch trong tuần hiện tại
    @Override
    public ApiResponse<Object> getTotalIncomeByWeek(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+1);
        Date starWeek = new Date(cal.getTimeInMillis());

        // lấy ngày kết thúc tuần
        cal.add(Calendar.DAY_OF_MONTH, 6);
        Date endWeek = new Date(cal.getTimeInMillis());

        Long total = transactionRepository.getTotalIncomeByMonthAndUsername(username, starWeek, endWeek);
        if(total == null)
        {
            return ApiResponse.builder().message("Không có giao dịch thu về trong tuần!").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền thu trong tuần").data(total).status(200).build();
    }

    // lấy tổng tiền chi tiêu của các giao dịch trong tuần hiện tại
    @Override
    public ApiResponse<Object> getTotalExpenseByWeek(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+1);
        Date starWeek = new Date(cal.getTimeInMillis());

        // lấy ngày kết thúc tuần
        cal.add(Calendar.DAY_OF_MONTH, 6);
        Date endWeek = new Date(cal.getTimeInMillis());

        Long total = transactionRepository.getTotalExpenseByMonthAndUsername(username, starWeek, endWeek);
        if(total == null)
        {
            return ApiResponse.builder().message("Không có giao dịch chi tiêu trong tuần").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền chi tiêu trong tuần").data(total).status(200).build();
    }

    // lấy danh sách các giao dịch trong tháng hiện tại
    @Override
    public ApiResponse<Object> getAllTransactionInCurrentMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());


        List<TransactionModel> list = transactionRepository.findAllByUserInfoModel_AccountModel_UsernameAndTransactiondateBetween(username, fromDate, toDate);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Không có giao dịch trong tháng!").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách các giao dịch trong tháng").status(200).data(list).build();
    }

    // lấy tổng tiền giao dịch thu về trong tháng hiện tại
    @Override
    public ApiResponse<Object> getTotalIncomeInCurrentMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long totalIncomeByMonth = transactionRepository.getTotalIncomeByMonthAndUsername(username, fromDate, toDate);
        if(totalIncomeByMonth == null)
        {
            return ApiResponse.builder().message("Không có giao dịch thu nhập trong tháng").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền thu nhập trong tháng").status(200).data(totalIncomeByMonth).build();
    }

    // lấy tổng tiền giao dịch chi tiêu trong tháng hiện tại
    @Override
    public ApiResponse<Object> getTotalExpenseInCurrentMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long totalIncomeByMonth = transactionRepository.getTotalExpenseByMonthAndUsername(username, fromDate, toDate);
        if(totalIncomeByMonth == null)
        {
            return ApiResponse.builder().message("Không có giao dịch thu chi tiêu trong tháng").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền chi tiêu trong tháng").status(200).data(totalIncomeByMonth).build();
    }

    // lấy ra tổng tiền thu nhập của tháng trước
    @Override
    public ApiResponse<Object> getTotalIncomeInPreviousMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());

        // lấy ngày cuối cùng của tháng trước
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long total = transactionRepository.getTotalIncomeByMonthAndUsername(username, fromDate, toDate);
        if(total==null)
        {
            return ApiResponse.builder().message("Không có giao dịch thu về trong tháng trước").status(101).build();
        }
        return ApiResponse.builder().message("Tông tiền thu về trong tháng trước").status(200).data(total).build();
    }

    // lấy tổng tiền chi tiêu trong tháng trước
    @Override
    public ApiResponse<Object> getTotalExpenseInPreviousMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());

        // lấy ngày cuối cùng của tháng trước
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long total = transactionRepository.getTotalExpenseByMonthAndUsername(username, fromDate, toDate);
        if(total==null)
        {
            return ApiResponse.builder().message("Không có giao dịch chi tiêu trong tháng trước").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền chi tiêu trong tháng trước").status(200).data(total).build();
    }

    // lấy chi tiết 1 giao dịch
    @Override
    public TransactionModel getTransaction(String username, Long idTransaction)
    {
        return transactionRepository.findTransactionModelByUserInfoModel_AccountModel_UsernameAndId(username, idTransaction);
    }

    // lấy danh sách giao dịch theo danh mục
    @Override
    public ApiResponse<Object> getAllByCategory(String username, Long categoryId, Date fromDate, Date toDate)
    {
        if(fromDate.compareTo(toDate) > 0)
        {
            return ApiResponse.builder().message("Ngày bắt đầu phải nhỏ hơn ngày kết thúc").status(100).build();
        }
        List<TransactionModel> list = transactionRepository.findAllByCategoryModel_Id(username,categoryId, fromDate, toDate);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Chưa có giao dịch theo danh mục").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách giao dịch theo danh mục").status(200).data(list).build();
    }

    // lấy tổng tiền của các giao dịch theo danh mục trong khoảng thời gian
    @Override
    public ApiResponse<Object> getTotalByCategory(String username, Long idCategory, Date fromDate, Date toDate)
    {
        Long total = transactionRepository.getTotalByCategory(username, idCategory, fromDate, toDate);
        if(total == null)
            return ApiResponse.builder().message("Chưa có giao dịch của danh mục").status(101).build();

        return ApiResponse.builder().message("Tổng tiền giao dịch theo danh mục").status(200).data(total).build();
    }

    @Override
    public ApiResponse<Object> getTotalByCategoryInMonth(String username, Long idCategory)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        Long total = transactionRepository.getTotalByCategory(username, idCategory, fromDate, toDate);
        if(total == null)
            return ApiResponse.builder().message("Chưa có giao dịch của danh mục").status(101).build();
        return ApiResponse.builder().message("Tổng tiền giao dịch theo danh mục").status(200).data(total).build();
    }

    // lấy list total theo danh mục thu nhập trong tháng hiện tại
    public ApiResponse<Object> getTotalByListCategoryIncomeInMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        List<Long> totalByCategoryIncomeInMonth = new ArrayList<>();
        List<Long> idCategoryIncome = new ArrayList<>();
        List<CategoryModel> categoryModels = categoryRepository.findAllByUserInfoModel_AccountModel_UsernameaAndAndType(username, 1L);
        for(CategoryModel item : categoryModels)
        {
            idCategoryIncome.add(item.getId());
        }
        for(Long i : idCategoryIncome)
        {
            Long total = transactionRepository.getTotalByCategory(username, i, fromDate, toDate);
            if(total == null)
            {
                totalByCategoryIncomeInMonth.add(0L);
            }
            else {
                totalByCategoryIncomeInMonth.add(total);
            }
        }
        return ApiResponse.builder().message("Tổng tiền theo danh mục giao dịch thu nhập").status(200).data(totalByCategoryIncomeInMonth).build();
    }

    // lấy list total theo danh mục chi tiêu trong tháng hiện tại
    public ApiResponse<Object> getTotalByListCategoryExpenseInMonth(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        List<Long> totalByCategoryExpenseInMonth = new ArrayList<>();
        List<Long> idCategoryExpense = new ArrayList<>();
        List<CategoryModel> categoryModels = categoryRepository.findAllByUserInfoModel_AccountModel_UsernameaAndAndType(username, 2L);
        for(CategoryModel item : categoryModels)
        {
            idCategoryExpense.add(item.getId());
        }
        for(Long i : idCategoryExpense)
        {
            Long total = transactionRepository.getTotalByCategory(username, i, fromDate, toDate);
            if(total == null)
            {
                totalByCategoryExpenseInMonth.add(0L);
            }
            else {
                totalByCategoryExpenseInMonth.add(total);
            }
        }
        return ApiResponse.builder().message("Tổng tiền theo danh mục giao dịch chi tiêu").status(200).data(totalByCategoryExpenseInMonth).build();
    }

    // ============== lấy danh sách thu nhập và chi tiêu của từng thẻ


    // lấy tổng tiền thu về của các giao dịch trong 1 tháng của 1 năm nào đó do người dùng chọn
    @Override
    public ApiResponse<Object> getTotalIncomeByMonth(String username, int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long totalIncomeByMonth = transactionRepository.getTotalIncomeByMonthAndUsername(username, fromDate, toDate);
        if(totalIncomeByMonth == null)
        {
            return ApiResponse.builder().message("Không có giao dịch thu về trong tháng").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền thu về trong tháng").status(200).data(totalIncomeByMonth).build();
    }


    // lấy tổng tiền chi tiêu của các giao dịch trong 1 tháng của 1 năm nào đó do người dùng chọn
    @Override
    public ApiResponse<Object> getTotalExpenseByMonth(String username, int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long totalExpense = transactionRepository.getTotalExpenseByMonthAndUsername(username, fromDate, toDate);
        if(totalExpense == null)
        {
            return ApiResponse.builder().message("Không có giao dịch chi tiêu trong tháng").status(101).build();
        }
        return ApiResponse.builder().message("Tổng tiền chi tiêu trong tháng").status(200).data(totalExpense).build();
    }

    // =============== tổng tiền giao dịch thu nhâ và chi tiêu theo từng tháng
    private Long getTotalIncomeWithMonth(String username, int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long totalIncome = transactionRepository.getTotalIncomeByMonthAndUsername(username, fromDate, toDate);
        if(totalIncome == null)
            return 0L;
        else
            return totalIncome;
    }

    public ApiResponse<Object> getTotalIncomeInYear(String username)
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<Long> listTotalWithYear = new ArrayList<>();
        for(int i = 1; i <= 12; i++)
        {
            Long total = getTotalIncomeWithMonth(username, year, i);
            listTotalWithYear.add(total);
        }
        return ApiResponse.builder().status(200).message("Danh sách thu nhập qua từng tháng").data(listTotalWithYear).build();
    }

    private Long getTotalExpenseWithMonth(String username, int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());

        Long totalExpense = transactionRepository.getTotalExpenseByMonthAndUsername(username, fromDate, toDate);
        if(totalExpense == null)
            return 0L;
        else
            return totalExpense;
    }

    public ApiResponse<Object> getTotalExpenseInYear(String username)
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<Long> listTotalWithYear = new ArrayList<>();
        for(int i = 1; i <= 12; i++)
        {
            Long total = getTotalExpenseWithMonth(username, year, i);
            listTotalWithYear.add(total);
        }
        return ApiResponse.builder().status(200).message("Danh sách chi tiêu qua từng tháng").data(listTotalWithYear).build();
    }


    // ========================================
    // lấy danh sách các giao dịch theo khoảng thời gian
    @Override
    public ApiResponse<Object> getTransactionFromTo(String username, Date from, Date to)
    {
        if(from.compareTo(to) > 0)
        {
            return ApiResponse.builder().message("Ngày bắt đầu phải nhỏ hơn ngày kết thúc").status(100).build();
        }
        List<TransactionModel> transactionModels = transactionRepository.findAllByUserInfoModel_AccountModel_UsernameAndTransactiondateBetween(username, from, to);
        if(transactionModels.size() == 0)
        {
            return ApiResponse.builder().message("Chưa có giao dịch trong khoảng thời gian này!").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách các giao dịch").status(200).data(transactionModels).build();
    }

    // tổng tiền thu nhập của tất cả giao dịch trong khoảng thời gian
    @Override
    public ApiResponse<Object> getTotalIncomeInTime(String username, Date from, Date to)
    {
        Long total = transactionRepository.getTotalIncomeByMonthAndUsername(username, from, to);
        if(total == null)
        {
            return ApiResponse.builder().message("Không có giao dịch trong khoảng thời gian").status(101).build();
        }
        return ApiResponse.builder().message("Tổng thu nhập trong khoảng thời gian").status(200).data(total).build();
    }

    @Override
    public ApiResponse<Object> getTotalExpenseInTime(String username, Date from, Date to)
    {
        Long total = transactionRepository.getTotalExpenseByMonthAndUsername(username, from, to);
        if(total == null)
        {
            return ApiResponse.builder().message("Không có giao dịch trong khoảng thời gian").status(101).build();
        }
        return ApiResponse.builder().message("Tổng chi tiêu trong khoảng thời gian").status(200).data(total).build();
    }

    //============================
    // tổng tiền thu nhập và chi tiêu theo thẻ ngân hàng trong tháng hiện tại
    @Override
    public ApiResponse<Object> getTotalIncomeByCardInMonth(String username, Long idCard)
    {
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        if(cardModel == null || cardModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy thẻ").status(101).build();
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        Long totalIncome = transactionRepository.totalByCard(username, idCard, 1L, fromDate, toDate);
        if(totalIncome == null)
        {
            return ApiResponse.builder().message("Thẻ chưa có giao dịch thu nhập").status(101).build();
        }

        return ApiResponse.builder().message("Tổng tiền thu nhập của thẻ").status(200).data(totalIncome).build();
    }

    @Override
    public ApiResponse<Object> getTotalExpenseByCardInMonth(String username, Long idCard)
    {
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        if(cardModel == null || cardModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy thẻ").status(101).build();
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        Long totalExpense = transactionRepository.totalByCard(username, idCard, 2L, fromDate, toDate);
        if(totalExpense == null)
        {
            return ApiResponse.builder().message("Thẻ chưa có giao dịch chi tiêu").status(101).build();
        }

        return ApiResponse.builder().message("Tổng tiền chi tiêu của thẻ").status(200).data(totalExpense).build();
    }


    // lấy ra list thu nhập và chi tiêu của các thẻ
    public ApiResponse<Object> getListTotalIncomeByCard(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        List<Long> idCard = new ArrayList<>();
        List<Long> listTotalIncome = new ArrayList<>();
        Iterable<CardModel> listCard = cardRepository.findAllByUserInfoModel_AccountModel_Username(username);

        for(CardModel item : listCard)
        {
            idCard.add(item.getId());
        }
        for(Long i : idCard)
        {
            Long total = transactionRepository.totalByCard(username, i, 1L,  fromDate, toDate);
            if(total == null)
            {
                listTotalIncome.add(0L);
            }
            else {
                listTotalIncome.add(total);
            }
        }
        return ApiResponse.builder().message("Danh sách thu nhập của các thẻ theo tháng").status(200).data(listTotalIncome).build();
    }


    public ApiResponse<Object> getListTotalExpenseByCard(String username)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        Date fromDate = new Date(cal.getTimeInMillis());
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        Date toDate = new Date(cal.getTimeInMillis());
        List<Long> idCard = new ArrayList<>();
        List<Long> listTotalExpense = new ArrayList<>();
        Iterable<CardModel> listCard = cardRepository.findAllByUserInfoModel_AccountModel_Username(username);

        for(CardModel item : listCard)
        {
            idCard.add(item.getId());
        }
        for(Long i : idCard)
        {
            Long total = transactionRepository.totalByCard(username, i, 2L,  fromDate, toDate);
            if(total == null)
            {
                listTotalExpense.add(0L);
            }
            else {
                listTotalExpense.add(total);
            }
        }
        return ApiResponse.builder().message("Danh sách thu nhập của các thẻ theo tháng").status(200).data(listTotalExpense).build();
    }


    // tổng tiền thu nhập và chi tiêu theo thẻ ngân hàng trong khoảng thời gian
    @Override
    public ApiResponse<Object> getTotalIncomeByCard(String username, Long idCard, Date fromDate, Date toDate)
    {
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        if(cardModel == null || cardModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy thẻ").status(101).build();
        }

        Long totalIncome = transactionRepository.totalByCard(username, idCard, 1L, fromDate, toDate);
        if(totalIncome == null)
        {
            return ApiResponse.builder().message("Thẻ chưa có giao dịch thu nhập").status(101).build();
        }

        return ApiResponse.builder().message("Tổng tiền thu nhập của thẻ").status(200).data(totalIncome).build();
    }

    @Override
    public ApiResponse<Object> getTotalExpenseByCard(String username, Long idCard, Date fromDate, Date toDate)
    {
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        if(cardModel == null || cardModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy thẻ").status(101).build();
        }

        Long totalExpense = transactionRepository.totalByCard(username, idCard, 2L, fromDate, toDate);
        if(totalExpense == null)
        {
            return ApiResponse.builder().message("Thẻ chưa có giao dịch chi tiêu").status(101).build();
        }

        return ApiResponse.builder().message("Tổng tiền chi tiêu của thẻ").status(200).data(totalExpense).build();
    }

    // lấy tất cả giao dịch theo id thẻ theo thời gian mà người dùng chọn
    @Override
    public ApiResponse<Object> getAllByCard(String username, Long idCard, Date fromDate, Date toDate)
    {
        if(fromDate.compareTo(toDate) > 0)
        {
            return ApiResponse.builder().message("Ngày bắt đầu phải nhỏ hơn ngày kết thúc").status(100).build();
        }
        List<TransactionModel> list = transactionRepository.findAllByCard(username, idCard, fromDate, toDate);
        if(list.size() == 0)
        {
            return ApiResponse.builder().message("Không có giao dịch theo thẻ").status(101).build();
        }
        return ApiResponse.builder().message("Danh sách giao dịch theo thẻ").status(200).data(list).build();
    }


}
