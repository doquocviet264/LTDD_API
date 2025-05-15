package com.ltdd.QuanLyThuChiAPI.Controller.Transaction;

import com.ltdd.QuanLyThuChiAPI.Model.Transaction.TransactionModel;
import com.ltdd.QuanLyThuChiAPI.Request.Transaction.TransactionRequest;
import com.ltdd.QuanLyThuChiAPI.Request.Transaction.UpdateTransactionRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Service.Transaction.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.sql.Date;

@Slf4j
@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/all")
    public ApiResponse<Object> getAll(Principal principal)
    {
        return transactionService.getAll(principal.getName());
    }

    @GetMapping(value = "/{id}")
    public Mono<TransactionModel> getTransaction(Principal principal, @PathVariable("id") Long idTransaction)
    {
        return Mono.just(transactionService.getTransaction(principal.getName(), idTransaction));
    }
    @GetMapping(value = "/income")
    public Mono<ApiResponse<Object>> incomeTransaction(Principal principal)
    {
        return Mono.just(transactionService.incomeTransaction(principal.getName()));
    }

    @GetMapping(value = "/expense")
    public Mono<ApiResponse<Object>> expenseTransaction(Principal principal)
    {
        return Mono.just(transactionService.expenseTransaction(principal.getName()));
    }

    @Transactional
    @PostMapping(value = "/add/income/{idCategory}/{idCard}")
    public Mono<ApiResponse<Object>> addTransactionIncome(Principal principal, @PathVariable("idCategory") Long idCategory, @PathVariable("idCard") Long idCard, @RequestBody TransactionRequest transactionRequest) {
        return Mono.just(transactionService.addTransactionIncome(principal.getName(), idCategory, idCard, transactionRequest));
    }

    @Transactional
    @PostMapping(value = "/add/expense/{idCategory}/{idCard}")
    public Mono<ApiResponse<Object>> addTransactionExpense(Principal principal, @PathVariable("idCategory") Long idCategory, @PathVariable("idCard") Long idCard, @RequestBody TransactionRequest transactionRequest) {
        return Mono.just(transactionService.addTransactionExpense(principal.getName(), idCategory, idCard, transactionRequest));
    }

    @PutMapping(value = "/update/{idCategory}/{idTransaction}")
    public Mono<ApiResponse<Object>> updateTransaction(Principal principal, @PathVariable("idCategory") Long idCategory, @PathVariable("idTransaction") Long idTransaction,@RequestBody UpdateTransactionRequest updateTransactionRequest) {
        return Mono.just(transactionService.updateTransaction(principal.getName(), idCategory, idTransaction, updateTransactionRequest));
    }

    @DeleteMapping(value = "/delete/{id}")
    public Mono<ApiResponse<Object>> deleteTransaction(Principal principal, @PathVariable("id") Long id)
    {
        return Mono.just(transactionService.deleteTransaction(principal.getName(), id));
    }

    // theo ngày
    @GetMapping(value = "/allincomeintoday")
    public ApiResponse<Object> getAllIncomeToday(Principal principal)
    {
        return transactionService.getAllIncomeInDayCurrent(principal.getName());
    }

    @GetMapping(value = "/allexpensetoday")
    public ApiResponse<Object> getAllExpenseToday(Principal principal)
    {
        return transactionService.getAllExpenseInDayCurrent(principal.getName());
    }

    @GetMapping(value = "/alltransactionintoday")
    public ApiResponse<Object> getAllTransactionToday(Principal principal)
    {
        return transactionService.getAllTransactionInDayCurrent(principal.getName());
    }

    @GetMapping(value = "/gettotalincometoday")
    public ApiResponse<Object> getTotalIncomeToday(Principal principal)
    {
        return transactionService.getTotalIncomeToday(principal.getName());
    }

    @GetMapping(value = "/gettotalexpensetoday")
    public ApiResponse<Object> getTotalExpenseToday(Principal principal)
    {
        return transactionService.getTotalExpenseToday(principal.getName());
    }

    // theo tuần
    @GetMapping(value = "/allofweek")
    public Mono<ApiResponse<Object>> getAllOfWeek(Principal principal)
    {
        return Mono.just(transactionService.getAllInWeek(principal.getName()));
    }

    @GetMapping(value = "/totalincomebyweek")
    public ApiResponse<Object> getTotalIncomeByWeek(Principal principal)
    {
        return transactionService.getTotalIncomeByWeek(principal.getName());
    }

    @GetMapping(value = "/totalexpensebyweek")
    public ApiResponse<Object> getTotalExpenseByWeek(Principal principal)
    {
        return transactionService.getTotalExpenseByWeek(principal.getName());
    }

    // theo tháng
    @GetMapping(value = "/allincurrentmonth")
    public ApiResponse<Object> getAllTransactionInCurrentMonth(Principal principal)
    {
        return transactionService.getAllTransactionInCurrentMonth(principal.getName());
    }

    @GetMapping(value = "/totalincome/currentmonth")
    public ApiResponse<Object> getTotalIncomeByCurrentMonth(Principal principal)
    {
        return transactionService.getTotalIncomeInCurrentMonth(principal.getName());
    }

    @GetMapping(value = "/totalexpense/currentmonth")
    public ApiResponse<Object> getTotalIncByCurrentMonth(Principal principal)
    {
        return transactionService.getTotalExpenseInCurrentMonth(principal.getName());
    }

    @GetMapping(value = "/totalincome/previousmonth")
    public ApiResponse<Object> getTotalIncomeInPreviousMonth(Principal principal)
    {
        return transactionService.getTotalIncomeInPreviousMonth(principal.getName());
    }

    @GetMapping(value = "/totalexpense/previousmonth")
    public ApiResponse<Object> getTotalExpenseInPreviousMonth(Principal principal)
    {
        return transactionService.getTotalExpenseInPreviousMonth(principal.getName());
    }

    // lấy tổng tiền giao dịch theo danh mục trong khoảng thời gian
    @GetMapping(value = "/totalbycategory/{idCategory}/{date1}/{date2}")
    public ApiResponse<Object> totalByCategoryInMonth(Principal principal, @PathVariable("idCategory") Long id, @PathVariable("date1") Date fromDate, @PathVariable("date2") Date toDate)
    {
        return transactionService.getTotalByCategory(principal.getName(), id, fromDate, toDate);
    }

    // lấy tổng tiền giao dịch theo danh mục trong tháng hiện tại để kiểm tra ngân sách
    @GetMapping(value = "/totalbycategory/{idCategory}")
    public ApiResponse<Object> totalByCategoryInMonth(Principal principal, @PathVariable("idCategory") Long id)
    {
        return transactionService.getTotalByCategoryInMonth(principal.getName(), id);
    }

    // lấy giao dịch theo 1 khoảng thời gian
    @GetMapping(value = "/from/{date1}/to/{date2}")
    public ApiResponse<Object> getTransactionFromTo(Principal principal, @PathVariable("date1") Date date1, @PathVariable("date2") Date date2)
    {
        return transactionService.getTransactionFromTo(principal.getName(), date1, date2);
    }

    // lấy tổng tiền chi tiêu và thu nhập theo 1 khoảng thời gian
    @GetMapping(value = "/totalincome/from/{date1}/to/{date2}")
    public ApiResponse<Object> getTotalIncomeInTime(Principal principal, @PathVariable("date1") Date fromDate, @PathVariable("date2") Date toDate)
    {
        return transactionService.getTotalIncomeInTime(principal.getName(), fromDate, toDate);
    }

    @GetMapping(value = "/totalexpense/from/{date1}/to/{date2}")
    public ApiResponse<Object> getTotalExpenseInTime(Principal principal, @PathVariable("date1") Date fromDate, @PathVariable("date2") Date toDate)
    {
        return transactionService.getTotalExpenseInTime(principal.getName(), fromDate, toDate);
    }

    // tổng thu nhập và chi tiêu theo từng tháng
    @GetMapping(value = "/totalincomebymonth/{year}/{month}")
    public ApiResponse<Object> getTotalIncomeByMonth(Principal principal, @PathVariable("year") int year, @PathVariable("month") int month)
    {
        return transactionService.getTotalIncomeByMonth(principal.getName(), year, month);
    }

    @GetMapping(value = "/totalexpensebymonth/{year}/{month}")
    public ApiResponse<Object> getTotalExpenseByMonth(Principal principal, @PathVariable("year") int year, @PathVariable("month") int month)
    {
        return transactionService.getTotalExpenseByMonth(principal.getName(), year, month);
    }

    // lấy tất cả giao dịch theo danh mục chi tiêu
    @GetMapping(value = "/category/{idCategory}/{fromDate}/{toDate}")
    public ApiResponse<Object> getAllByCategory(Principal principal, @PathVariable("idCategory") Long idCategory, @PathVariable("fromDate") Date fromDate, @PathVariable("toDate") Date toDate)
    {
        return transactionService.getAllByCategory(principal.getName(), idCategory, fromDate, toDate);
    }

    // lấy danh sách giao dịch theo thẻ ngân hàng
    @GetMapping(value = "/all/card/{id}/{fromDate}/{toDate}")
    public ApiResponse<Object> getAllIncomeByCard(Principal principal, @PathVariable("id") Long id, @PathVariable("fromDate") Date fromDate, @PathVariable("toDate") Date toDate)
    {
        return transactionService.getAllByCard(principal.getName(), id, fromDate, toDate);
    }

    // tổng thu nhập và chi tiêu của thẻ trong tháng
    @GetMapping(value = "/totalincome/card/{idCard}")
    public ApiResponse<Object> getTotalIncomeByCardInMonth(Principal principal, @PathVariable("idCard") Long idCard)
    {
        return transactionService.getTotalIncomeByCardInMonth(principal.getName(), idCard);
    }

    @GetMapping(value = "/totalexpense/card/{idCard}")
    public ApiResponse<Object> getTotalExpenseByCardInMonth(Principal principal, @PathVariable("idCard") Long idCard)
    {
        return transactionService.getTotalExpenseByCardInMonth(principal.getName(), idCard);
    }

    // tổng thu nhập và chi tiêu theo thẻ trong khoảng thời gian
    @GetMapping(value = "/totalincome/card/{id}/from/{date1}/to/{date2}")
    public ApiResponse<Object> getTotalIncomeByCard(Principal principal, @PathVariable("id") Long idCard, @PathVariable("date1") Date fromDate, @PathVariable("date2") Date toDate)
    {
        return transactionService.getTotalIncomeByCard(principal.getName(), idCard, fromDate, toDate);
    }

    @GetMapping(value = "/totalexpense/card/{id}/from/{date1}/to/{date2}")
    public ApiResponse<Object> getTotalExpenseByCard(Principal principal, @PathVariable("id") Long idCard, @PathVariable("date1") Date fromDate, @PathVariable("date2") Date toDate)
    {
        return transactionService.getTotalExpenseByCard(principal.getName(), idCard, fromDate, toDate);
    }


    // lấy ra list tổng tiền thu nhập qua các tháng
    @GetMapping(value = "/totalincomeinyear")
    public ApiResponse<Object> getTotalIncomeInYear(Principal principal)
    {
        return transactionService.getTotalIncomeInYear(principal.getName());
    }

    @GetMapping(value = "/totalexpenseinyear")
    public ApiResponse<Object> getTotalExpenseInYear(Principal principal)
    {
        return transactionService.getTotalExpenseInYear(principal.getName());
    }

    // lấy ra list thu nhập và chi tiêu theo danh mục thu nhập và chi tiêu
    @GetMapping(value = "/totalbycategoryincomeinmonth")
    public ApiResponse<Object> getTotalByCategoryIncomeInMonth(Principal principal)
    {
        return transactionService.getTotalByListCategoryIncomeInMonth(principal.getName());
    }

    @GetMapping(value = "/totalbycategoryexpenseinmonth")
    public ApiResponse<Object> getTotalByCategoryExpenseInMonth(Principal principal)
    {
        return transactionService.getTotalByListCategoryExpenseInMonth(principal.getName());
    }

    // lấy ra list tổng thu nhập và chi tiêu theo thẻ ngân hàng
    @GetMapping(value = "/listtotalincomebycard")
    public ApiResponse<Object> getListTotalIncomeByCardInMonth(Principal principal)
    {
        return transactionService.getListTotalIncomeByCard(principal.getName());
    }

    @GetMapping(value = "/listtotalexpensebycard")
    public ApiResponse<Object> getListTotalExpenseByCardInMonth(Principal principal)
    {
        return transactionService.getListTotalExpenseByCard(principal.getName());
    }
}
