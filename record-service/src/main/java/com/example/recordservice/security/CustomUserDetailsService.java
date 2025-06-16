package com.example.recordservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // TODO: 실제 사용자 정보를 DB에서 조회하도록 수정
        return new User(
                userId,
                "",  // 비밀번호는 JWT 토큰으로 인증하므로 필요 없음
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
} 