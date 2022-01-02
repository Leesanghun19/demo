package SenierProject.demo.Service;

import SenierProject.demo.domain.Member;
import SenierProject.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private  final MemberRepository memberRepository;
    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateEmail(member);
        memberRepository.save(member);
        return member.getId();
    }
    //이메일중복확인
    public void validateDuplicateEmail(Member member){
        List<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(!findMember.isEmpty()){
            throw new IllegalStateException("이미 존제하는 이메일");
        }
    }
    //회원조회
    public List<Member> findMembers(){return memberRepository.findALl();}
    public Member findOne(Long memberId) {return memberRepository.findById(memberId).get();}

    //삭제
    @Transactional
    public void deleteMember(Long memberId){memberRepository.deleteMember(memberId);}
}
