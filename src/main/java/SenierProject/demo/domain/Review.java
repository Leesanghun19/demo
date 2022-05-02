package SenierProject.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    @Column(name="review_id")
    private Long id;

    private String texts;//리뷰내용
    private String retext;//답변

    private LocalDateTime reviewDate;
    private LocalDateTime updateDate;

    @Enumerated(EnumType.ORDINAL)
    private ReviewStar reviewStar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<Photo> photos = new ArrayList<>();

}
