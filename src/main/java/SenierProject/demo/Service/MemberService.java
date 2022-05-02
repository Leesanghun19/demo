package SenierProject.demo.Service;

import SenierProject.demo.domain.Member;
import SenierProject.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private  final MemberRepository memberRepository;
    //회원가입
    @Transactional
    public Long join(String nickName, String passWord, String email){
        Member member = new Member(nickName,passWord,email);
        validateDuplicateEmail(member);
        memberRepository.save(member);
        return member.getId();
    }
    //이메일중복확인
    public void validateDuplicateEmail(Member member){
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if(!findMember.isEmpty()){
            throw new IllegalStateException("이미 존제하는 이메일");
        }
    }
    //회원조회
    public List<Member> findMembers(){return memberRepository.findAll();}
    public Member findById(Long memberId) {return memberRepository.findById(memberId).get();}
    @Transactional
    public void updateMember(Long memberId,String password,String nickName){
        Member member = memberRepository.findById(memberId).get();
        member.updatePassWord(password);
        member.updateNickName(nickName);

    }
    //삭제
    @Transactional
    public void deleteMember(Long memberId){memberRepository.deleteById(memberId);}
}
