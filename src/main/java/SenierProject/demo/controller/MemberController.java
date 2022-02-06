package SenierProject.demo.controller;

import SenierProject.demo.Service.MemberService;
import SenierProject.demo.domain.Member;
import SenierProject.demo.repository.EmailRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {
    /**

     */
    private  final MemberService memberService;
    private final EmailRepository emailRepository;
    /**
    모든회원이름 조회
     */
    @GetMapping("/members")
    public Result memberAll(){
        List<Member> findMembers=memberService.findMembers();
        List<MemberDto>collect = findMembers.stream()
                .map(m->new MemberDto(m.getNickName(),m.getEmail()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    /**
    단일회원조회
     */
    @GetMapping("/member/{id}")
    public Result memberId(
            @PathVariable("id")Long id
    ){
        Member findMemberId=memberService.findOne(id);
        MemberDto m= new MemberDto(findMemberId.getNickName(),findMemberId.getEmail());
        return new Result(m);
    }
    /**
    회원등록
     */
    @PostMapping("/member")
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request){
        if(emailRepository.findByEmail(request.getEmail()).get().isCheck()==true) {
            Member member = Member.builder()
                    .nickName(request.getNickName())
                    .email(request.getEmail())
                    .createDate(LocalDateTime.now()).build();

            Long id = memberService.join(member);
            return new CreateMemberResponse(id);
        }
        else
            return new CreateMemberResponse(-1L);
    }
    /**
    회원삭제
     */
    @DeleteMapping("/member/{id}")
    public DeleteMember deleteMember(
            @PathVariable("id")Long id
    ){
        memberService.deleteMember(id);
        return new DeleteMember(id);
    }



    /**
    DTO
     */

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class DeleteMember{
        private Long id;

    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String nickName;
        private String email;
    }
    @Data
    @AllArgsConstructor
    static class MemberIdDto {
        private String nickName;

    }
    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String nickName;
        @NotEmpty
        private String email;
    }
}

