package com.byondxtremes.ecom.auth.mapper;

import com.byondxtremes.ecom.auth.config.JwtTokenUtil;
import com.byondxtremes.ecom.auth.domain.dto.JwtRequest;
import com.byondxtremes.ecom.auth.domain.dto.JwtResponse;
import com.byondxtremes.ecom.auth.domain.dto.SignUpRequest;
import com.byondxtremes.ecom.auth.domain.entity.Role;
import com.byondxtremes.ecom.auth.domain.entity.UserEntity;
import com.byondxtremes.ecom.auth.enums.ERole;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(
    componentModel = "spring"
)
public interface UserMapper {

    default UserEntity toUserEntity(SignUpRequest signUpRequest,
                                    BCryptPasswordEncoder bCryptPasswordEncoder) {
        return UserEntity.builder().email(signUpRequest.getEmailId())
            .username(signUpRequest.getEmailId().substring(0,
                signUpRequest.getEmailId().indexOf("@")))
            .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
            .roles(List.of(
                Role.builder().id(UUID.randomUUID().toString()).name(ERole.ROLE_ADMIN).build()))
            .build();
    }

    default JwtResponse toJwtResponse(String token, JwtRequest jwtRequest,
                                      JwtTokenUtil jwtTokenUtil) {
        JwtResponse pe = new JwtResponse(token);
        pe.setUsername(jwtRequest.getUsername());
        pe.setIssuedAt(jwtTokenUtil.getIssuedAtDateFromToken(token));
        pe.setExpireAt(jwtTokenUtil.getExpirationDateFromToken(token));
        pe.setIsTokenExpired(jwtTokenUtil.isTokenExpired(token));
        return pe;
    }
}
