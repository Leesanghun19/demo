package SenierProject.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="business_id")
    private Business business;

    @OneToOne(mappedBy = "store")
    private Photo photo;
}
