package SenierProject.demo.Service;

import SenierProject.demo.domain.Review;
import SenierProject.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public Review findIdWithStore(Long id){return reviewRepository.findIdWithStore(id);}
    public Review findIdWithMember(Long id){return reviewRepository.findIdWithMember(id);}

    //삭제
    @Transactional
    public void deleteReview(Long reviewId){reviewRepository.deleteReview(reviewId);}
    //수정
    @Transactional
    public void update(Long id,String text,String reText){
        Review review=reviewRepository.findById(id).get();
        review.setText(text);
        review.setRetext(reText);
        review.setUpdate(LocalDateTime.now());
    }
}
