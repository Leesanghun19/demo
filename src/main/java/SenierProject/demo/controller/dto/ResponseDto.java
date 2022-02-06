package SenierProject.demo.controller.dto;

import lombok.Data;

@Data
public class ResponseDto {

    private String response;
    public ResponseDto(String email){
        this.response =email;
    }
}
