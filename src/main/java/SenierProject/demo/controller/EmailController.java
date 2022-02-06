package SenierProject.demo.controller;

import SenierProject.demo.Service.EmailService;
import SenierProject.demo.controller.dto.EmailRequest;
import SenierProject.demo.controller.dto.ResponseDto;
import SenierProject.demo.controller.dto.VerifyRequest;
import SenierProject.demo.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;
    private final EmailRepository emailRepository;
    @PostMapping("/email") // 이메일 인증 코드 보내기
    public ResponseDto emailAuth(@RequestBody @Valid EmailRequest email) throws Exception {
        emailService.sendSimpleMessage(email.getEmail());
        return new ResponseDto(email.getEmail());
    }

    @PostMapping("/verifyCode/{email}") // 이메일 인증 코드 검증
    public ResponseDto verifyCode(@PathVariable("email")String email, @RequestBody VerifyRequest code) {
        if(emailRepository.findByEmail(email).get().getCode().equals(code.getCode())) {
            emailService.isChecked(email);
            return new ResponseDto(code.getCode());
        }
        else{
            return new ResponseDto("불일치");
        }
    }
}