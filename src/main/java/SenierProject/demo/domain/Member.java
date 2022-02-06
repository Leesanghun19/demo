package SenierProject.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class Member {
    @Id
    @GeneratedValue
    @Column(name="member_id")
    public Long id;

    public String nickName;
    private Long phoneNumber;
    public String email;
    public String password;
    private LocalDateTime createDate;
    private Boolean emailAuth;


    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public void emailVerifiedSuccess() {
        this.emailAuth = true;
    }

}
