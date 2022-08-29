package com.yoshidainasaku.output.bbsdemo.service;

import com.yoshidainasaku.output.bbsdemo.persistence.entity.LoginUser;
import com.yoshidainasaku.output.bbsdemo.persistence.repository.LoginUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserDetailsService implements UserDetailsService {
    private final LoginUserRepository loginUserRepository;

    @Autowired
    public LoginUserDetailsService(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<LoginUser> loginUserOptional = loginUserRepository.findByUserId(userId);
        return loginUserOptional.map(LoginUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
    }
}
