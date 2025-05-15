package com.ltdd.QuanLyThuChiAPI.Service.Card;

import com.ltdd.QuanLyThuChiAPI.Interface.Card.CardInterface;
import com.ltdd.QuanLyThuChiAPI.Model.Card.CardModel;
import com.ltdd.QuanLyThuChiAPI.Model.Transaction.TransactionModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.AccountModel;
import com.ltdd.QuanLyThuChiAPI.Model.User.UserInfoModel;
import com.ltdd.QuanLyThuChiAPI.Repository.Card.CardRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.AccountRepository;
import com.ltdd.QuanLyThuChiAPI.Repository.User.UserInfoRepository;
import com.ltdd.QuanLyThuChiAPI.Request.Card.CardRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Utils.CurrentDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CardService implements CardInterface {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public CardModel getCard(String username, Long cardId)
    {
        return cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, cardId);
    }

    @Override
    public Iterable<CardModel> getAllCard(String username)
    {
        return cardRepository.findAllByUserInfoModel_AccountModel_Username(username);
    }

    @Override
    public ApiResponse<Object> addCard(String username, CardRequest cardRequest)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        if(accountModel == null || accountModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Accont khong ton tai!").status(101).build();
        }
        Optional<UserInfoModel> userInfoModel =  Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
        if (!userInfoModel.isPresent()) {
            return ApiResponse.builder().message("User không tồn tại!").status(101).build();
        }
        CardModel cardModel = CardModel.builder().userInfoModel(userInfoModel.get()).name(cardRequest.getName())
                .balance(cardRequest.getBalance())
                .cardnumber(cardRequest.getCardnumber())
                .description(cardRequest.getDescription())
                .created_at(CurrentDateTime.getCurrentDateTime())
                .updated_at(CurrentDateTime.getCurrentDateTime()).build();
        cardRepository.save(cardModel);
        return ApiResponse.builder().status(200).message("Tạo thẻ ngân hàng thành công!").data(cardModel).build();
    }

    @Override
    public ApiResponse<Object> updateCard(String username, CardRequest cardRequest, Long idCard)
    {
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        if(cardModel == null || cardModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không thể cập nhật thẻ ngân hàng!").status(101).build();
        }
        else {
            if (cardRequest.getName() != null && !cardRequest.getName().isEmpty()) {
                cardModel.setName(cardRequest.getName());
            }
            if (cardRequest.getBalance() != null && !cardRequest.getBalance().toString().isEmpty()) {
                cardModel.setBalance(cardRequest.getBalance());
            }
            if (cardRequest.getCardnumber() != null && !cardRequest.getCardnumber().isEmpty()) {
                cardModel.setCardnumber(cardRequest.getCardnumber());
            }
            if (cardRequest.getDescription() != null && !cardRequest.getDescription().isEmpty()) {
                cardModel.setDescription(cardRequest.getDescription());
            }
        }
        cardModel.setUpdated_at(CurrentDateTime.getCurrentDateTime());
        cardRepository.save(cardModel);
        return ApiResponse.builder().status(200).message("Cập nhật thẻ thành công").data(cardModel).build();
    }

    @Override
    public ApiResponse<Object> deleteCard(String username, Long idCard)
    {
        CardModel cardModel = cardRepository.findCardModelByUserInfoModel_AccountModel_UsernameAndId(username, idCard);
        if(cardModel == null || cardModel.getId() <= 0)
        {
            return ApiResponse.builder().message("Không tìm thấy thẻ!").status(101).build();
        }
        Long transactionCount = cardRepository.countTransactionsByCardId(username, idCard);
        if(transactionCount > 0)
            return ApiResponse.builder().message("Không thẻ xóa thẻ do thẻ do có giao dịch liên quan!").status(101).build();
        cardRepository.delete(cardModel);
        return ApiResponse.builder().message("Xóa thẻ thành công!").status(200).build();
    }

    @Override
    public List<TransactionModel> getTransactionByCard(String username, Long idCard)
    {
        return cardRepository.findAllTractionModelByUserInfoModel_AccountModel_UsernameAndId(username,idCard);
    }

}
