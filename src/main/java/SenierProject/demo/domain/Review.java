package SenierProject.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
public class Review {
    @Id@GeneratedValue
    @Column(name="review_id")
    private Long id;

    private String text;//리뷰내용
    private String retext;//답변

    private LocalDateTime reviewDate;
    private LocalDateTime update;

    @Enumerated(EnumType.ORDINAL)
    private ReviewStar reviewStar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;



}
