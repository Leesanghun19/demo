package SenierProject.demo.Service;

import SenierProject.demo.repository.BusinessRepository;
import SenierProject.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BusinessRepository businessRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws IllegalArgumentException {
        if (!memberRepository.findById(Long.parseLong(username)).isEmpty()) {
            return memberRepository.findById(Long.parseLong(username))
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        }
        else {
            return businessRepository.findById(Long.parseLong(username))
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        }
    }
}