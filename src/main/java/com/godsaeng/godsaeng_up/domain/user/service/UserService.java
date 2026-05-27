package com.godsaeng.godsaeng_up.domain.user.service;

import com.godsaeng.godsaeng_up.domain.profile.entity.Profile;
import com.godsaeng.godsaeng_up.domain.user.dto.RegisterRequest;
import com.godsaeng.godsaeng_up.domain.profile.repository.ProfileRepository;
import com.godsaeng.godsaeng_up.domain.user.entity.User;
import com.godsaeng.godsaeng_up.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(RegisterRequest request) {
        validatePasswordConfirm(request);
        validateDuplicateLoginId(request.loginId());
        validateDuplicateNickname(request.nickname());

        User user = new User(request.loginId(), passwordEncoder.encode(request.password()));
        userRepository.save(user);

        Profile profile = new Profile(user.getId(), request.nickname());
        profileRepository.save(profile);
    }

    private void validatePasswordConfirm(RegisterRequest request) {
        if(!request.password().equals(request.passwordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }
    }

    private void validateDuplicateLoginId(String loginId) {
        if(userRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if(profileRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }
}
