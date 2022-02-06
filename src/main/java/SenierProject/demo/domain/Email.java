package SenierProject.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    @Id
    @GeneratedValue
    @Column(name="email_id")
    public Long id;

    private String email;

    private String code;

    private boolean isCheck;

    public Email(String email,String code){
        this.email=email;
        this.code=code;
        this.isCheck=false;
    }
    public void IsChecked(){
        this.isCheck=true;
    }
}
