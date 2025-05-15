package com.ltdd.QuanLyThuChiAPI.Controller.Card;

import com.ltdd.QuanLyThuChiAPI.Model.Card.CardModel;
import com.ltdd.QuanLyThuChiAPI.Model.Transaction.TransactionModel;
import com.ltdd.QuanLyThuChiAPI.Request.Card.CardRequest;
import com.ltdd.QuanLyThuChiAPI.Response.Api.ApiResponse;
import com.ltdd.QuanLyThuChiAPI.Service.Card.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping(value = "/{id}")
    public Mono<CardModel> getCard(Principal principal, @PathVariable("id") Long id)
    {
        return Mono.just(cardService.getCard(principal.getName(),id));
    }

    @GetMapping(value = "/all")
    public Iterable<CardModel> getALLCard(Principal principal){
        return cardService.getAllCard(principal.getName());
    }

    @PostMapping(value = "/add")
    @Transactional
    public Mono<ApiResponse<Object>> addCard(Principal principal, @RequestBody CardRequest cardRequest)
    {
        return Mono.just(cardService.addCard(principal.getName(), cardRequest));
    }

    @PutMapping(value = "/update/{id}")
    public Mono<ApiResponse<Object>> updateCard(Principal principal, @RequestBody CardRequest cardRequest, @PathVariable("id") Long idCart)
    {
        return Mono.just(cardService.updateCard(principal.getName(), cardRequest, idCart));
    }

    @DeleteMapping(value = "/delete/{id}")
    public Mono<ApiResponse<Object>> deleteCard(Principal principal, @PathVariable("id") Long idCard)
    {
        return Mono.just(cardService.deleteCard(principal.getName(), idCard));
    }

    // lấy danh sách các giao dịch của 1 thẻ
    @GetMapping(value = "/gettransaction/{id}")
    public Mono<List<TransactionModel>> getTransactionByCardId(Principal principal, @PathVariable("id") Long idCard)
    {
        return Mono.just(cardService.getTransactionByCard(principal.getName(), idCard));
    }
}
