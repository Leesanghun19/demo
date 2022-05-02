package SenierProject.demo.Service;

import SenierProject.demo.domain.Review;
import SenierProject.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    //추가
    @Transactional
    public Long join(Review review){
        reviewRepository.save(review);
        return review.getId();
    }

    //조회
    public Review findOne(Long id){return reviewRepository.findById(id).get();}
    public Review findIdWithStore(Long id){return reviewRepository.findIdWithStore(id).get();}
    public Review findIdWithMember(Long id){return reviewRepository.findIdWithMember(id).get();}

    //삭제
    @Transactional
    public void deleteReview(Long reviewId){reviewRepository.deleteReview(reviewId);}
    //수정
    @Transactional
    public void update(Long id,String text,String retext){
        Review review=reviewRepository.findById(id).get();
        if(!(text==null)) {
            review.setTexts(text);
        }
        review.setRetext(retext);
        review.setUpdateDate(LocalDateTime.now());
    }
}
