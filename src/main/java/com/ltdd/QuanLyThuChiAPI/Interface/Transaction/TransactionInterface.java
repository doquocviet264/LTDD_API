package com.ltdd.QuanLyThuChiAPI.Interface.Transaction;

import com.ltdd.QuanLyThuChiAPI.Model.Transaction.TransactionModel;
import com.ltdd.QuanLyThuChiAPI.Request.Transaction.TransactionRequest;
import com.ltdd.QuanLyThuChiAPI.Request.Transaction.UpdateTransactionRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;

import java.sql.Date;

public interface TransactionInterface {

    // lấy tất cả giao dịch
    public ApiResponse<Object> getAll(String username);

    // theo ngày
    public ApiResponse<Object> getAllIncomeInDayCurrent(String username);
    public ApiResponse<Object> getAllExpenseInDayCurrent(String username);
    public ApiResponse<Object> getAllTransactionInDayCurrent(String username);
    public ApiResponse<Object> getTotalIncomeToday(String username);

    public ApiResponse<Object> getTotalExpenseToday(String username);

    // theo tuần
    public ApiResponse<Object> getAllInWeek(String usename);

    public ApiResponse<Object> getTotalIncomeByWeek(String username);

    public ApiResponse<Object> getTotalExpenseByWeek(String username);

    // theo tháng
    public ApiResponse<Object> getAllTransactionInCurrentMonth(String username);
    public ApiResponse<Object> getTotalIncomeInCurrentMonth(String username);

    public ApiResponse<Object> getTotalExpenseInCurrentMonth(String username);

    public ApiResponse<Object> getTotalIncomeInPreviousMonth(String username);

    public ApiResponse<Object> getTotalExpenseInPreviousMonth(String username);

    // ===========
    public ApiResponse<Object> incomeTransaction(String username);
    public ApiResponse<Object> expenseTransaction(String username);

    public ApiResponse<Object> addTransactionIncome(String username, Long idCategory,Long idCard, TransactionRequest transactionRequest);

    public ApiResponse<Object> addTransactionExpense(String username, Long idCategory,Long idCard, TransactionRequest transactionRequest);

    public ApiResponse<Object> updateTransaction(String username,Long idCategory,Long idTransaction, UpdateTransactionRequest updateTransactionRequest);

    public ApiResponse<Object> deleteTransaction(String username, Long idTransaction);

    public TransactionModel getTransaction(String username, Long idTransaction);

    // lấy tất cả giao dịch theo category
    public ApiResponse<Object> getAllByCategory(String username, Long categoryId, Date fromDate, Date toDate);

    // lấy tổng tiền của các giao dịch theo 1 danh mục chi tiêu theo khoảng thời gian
    public ApiResponse<Object> getTotalByCategory(String username, Long idCategory, Date fromDate, Date toDate);
    public ApiResponse<Object> getTotalByCategoryInMonth(String username, Long idCategory);

    public ApiResponse<Object> getTransactionFromTo(String username, Date from, Date to);

    public ApiResponse<Object> getTotalExpenseInTime(String username, Date from, Date to);

    public ApiResponse<Object> getTotalIncomeInTime(String username, Date from, Date to);

    public ApiResponse<Object> getTotalIncomeByMonth(String username, int year, int month);

    public ApiResponse<Object> getTotalExpenseByMonth(String username, int year, int month);

    // tổng tiền thu nhập và chi tiêu của thẻ trong tháng hiện tại


    // tổng tiền thu nhập và chi tiêu của thẻ theo tháng
    public ApiResponse<Object> getTotalIncomeByCardInMonth(String username, Long idCard);
    public ApiResponse<Object> getTotalExpenseByCardInMonth(String username, Long idCard);

    // tổng tiền thu nhập và chi tiêu của thẻ theo thời gian
    public ApiResponse<Object> getTotalIncomeByCard(String username, Long idCard, Date fromDate, Date toDate);

    public ApiResponse<Object> getTotalExpenseByCard(String username, Long idCard, Date fromDate, Date toDate);

    // lấy danh sách giao dịch theo thẻ ngân hàng
    public ApiResponse<Object> getAllByCard(String username, Long idCard, Date fromDate, Date toDate);
}
