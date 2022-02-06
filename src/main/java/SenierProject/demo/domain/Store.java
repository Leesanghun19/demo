package SenierProject.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue
    @Column(name="store_id")
    private Long id;
    private String phoneNumber;
    private String name;
    private String foodOrigin;//원산지
    private String introduce;//가게 소개
    private String number;//사업자등록번호

    @OneToMany(mappedBy = "store")
    private List<Food> food = new ArrayList<>();


}
