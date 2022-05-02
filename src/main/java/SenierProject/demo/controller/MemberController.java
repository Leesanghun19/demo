package SenierProject.demo.controller;

import SenierProject.demo.Service.EmailService;
import SenierProject.demo.Service.MemberService;
import SenierProject.demo.domain.Business;
import SenierProject.demo.domain.Member;
import SenierProject.demo.domain.Review;
import SenierProject.demo.jwt.JwtTokenProvider;
import SenierProject.demo.repository.BusinessRepository;
import SenierProject.demo.repository.EmailRepository;
import SenierProject.demo.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailRepository emailRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BusinessRepository businessRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 맴버조회
     */
    @GetMapping("/member")
    public Result memberOne(
            HttpServletRequest request2
    ) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);
        MemberOneDTO memberOneDTO = new MemberOneDTO(member);
        return new Result(memberOneDTO);
    }
    /**
     * 맴버들조회(관리자)
     */
    @GetMapping("/admin/members")
    public Result members(

    ) {

        List<Member> members = memberRepository.findAll();
        List<MemberOneDTO> collect = members.stream()
                .map(o -> new MemberOneDTO(o)).collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * 맴버등록
     */
    @PostMapping("/register")
    public ReturnMemberIdResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {


        if (emailRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new NoSuchElementException();
        } else {
            if (emailRepository.findByEmail(request.getEmail()).get().isCheck() == true
                    && emailRepository.findByEmail(request.getEmail()).get().isJoin()==false) {
                emailRepository.findByEmail(request.getEmail()).get().isJoined();
                return new ReturnMemberIdResponse(
                        memberService.join(request.getNickName(), passwordEncoder.encode(request.getPassword()), request.getEmail()));
            } else

                throw new NoSuchElementException();
        }

    }

    /**
     * 맴버업데이트(비밀번호변경)(관리자)
     */
    @PatchMapping("/admin/member/{id}")
    public ReturnMemberIdResponse updateMemberRoot(
            @PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.updateMember(id,passwordEncoder.encode(request.getPassword()), request.getNickName());
        return new ReturnMemberIdResponse(id);
    }

    /**
     * 맴버삭제(관리자)
     */
    @DeleteMapping("/admin/member/{id}")
    public DeleteMember deleteMemberRoot(
            @PathVariable("id") Long id
    ) {
        String email = memberService.findById(id).getEmail();
        emailRepository.deleteById(emailRepository.findByEmail(email).get().getId());
        memberService.deleteMember(id);
        return new DeleteMember(id);
    }
    /**
     * 맴버업데이트(비밀번호변경)
     */
    @PatchMapping("/member")
    public ReturnMemberIdResponse updateMember(
            HttpServletRequest request2, @RequestBody @Valid UpdateMemberRequest request) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));

        memberService.updateMember(userPk,passwordEncoder.encode(request.getPassword()), request.getNickName());
        return new ReturnMemberIdResponse(userPk);
    }

    /**
     * 맴버삭제
     */
    @DeleteMapping("/member")
    public DeleteMember deleteMember(
            HttpServletRequest request2
    ) {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        String email = memberService.findById(userPk).getEmail();
        emailRepository.deleteById(emailRepository.findByEmail(email).get().getId());
        memberService.deleteMember(userPk);
        return new DeleteMember(userPk);
    }
    /**
     * 로그인
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginMemberRequest member) {
        Member mem = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        Member mem1 = memberRepository.findByEmail(member.getEmail()).get();
        if (!passwordEncoder.matches(member.getPassword(), mem1.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");}
        Iterator<String> iter =mem1.getRoles().iterator();
        List<String> roles=new ArrayList<>();
        while (iter.hasNext()) {
            roles.add(iter.next());
        }
        return jwtTokenProvider.createToken(mem1.getUsername(), roles);
    }
    @PostMapping("/login/business")
    public String loginBusiness(@RequestBody LoginMemberRequest member) {
        Business mem = businessRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        Business mem1 = businessRepository.findByEmail(member.getEmail()).get();
        if (!passwordEncoder.matches(member.getPassword(), mem1.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");}
        Iterator<String> iter =mem1.getRoles().iterator();
        List<String> roles=new ArrayList<>();
        while (iter.hasNext()) {
            roles.add(iter.next());
        }
        return jwtTokenProvider.createToken(mem1.getUsername(), roles);
    }
    /**
     * DTO
     */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    public static class MemberOneDTO{
        private Long id;
        private String nickName;
        private String email;
        private Long reviewId;
        private List<Review> reviewList;
        public MemberOneDTO(Member member){
            id= member.getId();;
            nickName= member.getNickName();
            email= member.getEmail();;

        }
    }

    @Data
    public static class ReturnMemberIdResponse {
        private Long id;

        public ReturnMemberIdResponse(Long id) {
            this.id = id;
        }
    }
    @Data
    public static class UpdateMemberRequest {
        private String password;
        private String nickName;


    }
    @Data
    public static class LoginMemberRequest {
        private String email;
        private String password;
    }
    @Data
    public static class CreateMemberRequest {
        @NotEmpty
        private String nickName;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;
    }
    @Data
    @AllArgsConstructor
    public static class DeleteMember{
        private Long id;

    }
}

