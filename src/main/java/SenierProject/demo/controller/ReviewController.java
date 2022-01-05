package SenierProject.demo.controller;

import SenierProject.demo.Service.FoodService;
import SenierProject.demo.Service.MemberService;
import SenierProject.demo.Service.ReviewService;
import SenierProject.demo.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private ReviewService reviewService;
    private FoodService foodService;
    private MemberService memberService;


    @PostMapping("/review/{foodId}/{memberId}")
    public CreateReviewResponse createReview(
            @PathVariable("foodId")Long foodId,@PathVariable("memberId")Long memberId, @RequestBody @Valid CreateReviewRequest request){
        Review review= new Review();
        review.setFood(foodService.findId(foodId));
        review.setText(request.getText());
        review.setReviewDate(LocalDateTime.now());
        review.setReviewStar(request.getReviewStar());
        review.setMember(memberService.findOne(memberId));
        Long join = reviewService.join(review);
        return new CreateReviewResponse(join);

    }
    @PatchMapping("/review/{id}")
    public ReText reText(@PathVariable("id")Long id,@RequestBody @Valid ReText request){
        Review review = reviewService.findOne(id);
        review.setRetext(request.getReText());
        return new ReText(request.getReText());
    }
    @DeleteMapping("/review/{id}")
    public DeleteReviewResponse deleteFoodResponse(
                @PathVariable("id")Long id
    ){
            reviewService.deleteReview(id);
            return new DeleteReviewResponse(id);
    }

    @Data
    @AllArgsConstructor
    static class DeleteReviewResponse {
        Long id;
    }

    @Data
    @AllArgsConstructor
    static class CreateReviewResponse{
            Long id;
    }
    @Data
    static class CreateReviewRequest{
        String text;
        ReviewStar reviewStar;

    }

    @Data
    @AllArgsConstructor
    static class ReText{
        private String reText;

    }

    @Data
    static class ReviewByStore {
        private Long id;
        private String text;//리뷰내용
        private String retext;//답변
        private LocalDateTime reviewDate;
        private LocalDateTime update;
        private ReviewStar reviewStar;
        private String food;
        private String member;
        public ReviewByStore(Review review){
            id=review.getId();
            text=review.getText();
            retext=review.getRetext();
            reviewDate=review.getReviewDate();
            update=review.getUpdate();
            reviewStar=review.getReviewStar();
            food=review.getFood().getName();
            member=review.getMember().getNickName();
        }
    }






}
