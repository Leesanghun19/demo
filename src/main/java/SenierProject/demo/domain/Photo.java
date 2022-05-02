package SenierProject.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
public class Photo {
    @Id
    @GeneratedValue
    @Column(name="photo_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private PhotoType photoType;
    private String uploadFileName;
    private String storeFileName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Photo(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.photoType=PhotoType.서브;
    }
    public void setFood(Food food){
        this.food=food;
    }
    public void setReview(Review review){
        this.review=review;
    }
    public void setMain(){
        this.photoType=PhotoType.메인;
    }
    public void setSub(){this.photoType=PhotoType.서브;}}
