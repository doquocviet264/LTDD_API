package com.ltdd.QuanLyThuChiAPI.Interface.Card;

import com.ltdd.QuanLyThuChiAPI.Model.Card.CardModel;
import com.ltdd.QuanLyThuChiAPI.Model.Transaction.TransactionModel;
import com.ltdd.QuanLyThuChiAPI.Request.Card.CardRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;

import java.util.List;

public interface CardInterface {
    public CardModel getCard(String username, Long cardId);
    public Iterable<CardModel> getAllCard(String username);
    public ApiResponse<Object> updateCard(String username, CardRequest cardRequest, Long idCard);
    public ApiResponse<Object> addCard(String username, CardRequest cardRequest);

    public ApiResponse<Object> deleteCard(String username, Long idCard);
    public List<TransactionModel> getTransactionByCard(String username, Long idCard);
}
