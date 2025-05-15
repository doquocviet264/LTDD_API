//package com.ltdd.QuanLyThuChiAPI.Security.JWT.Token;
//
//import com.ltdd.QuanLyThuChiAPI.Repository.User.TokenRepository;
//
//public class TokenService {
//    private final TokenRepository tokenRepository;
//
//    public TokenService(TokenRepository tokenRepository) {
//        this.tokenRepository = tokenRepository;
//    }
//
//    public boolean isValidToken(String jti) {
//        return tokenRepository.findByToken_id(jti) != null;
//    }
//}
