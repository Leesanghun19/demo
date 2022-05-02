package SenierProject.demo.controller;

import SenierProject.demo.Service.FoodService;
import SenierProject.demo.Service.MemberService;
import SenierProject.demo.Service.PhotoService;
import SenierProject.demo.Service.ReviewService;
import SenierProject.demo.domain.*;
import SenierProject.demo.jwt.JwtTokenProvider;
import SenierProject.demo.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final FoodService foodService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;
    /**
     리뷰등록
     */
    @PostMapping("/review/{foodid}")
    public CreateReviewResponse createReview(
            @PathVariable("foodid")Long foodid, @RequestBody @Valid CreateReviewRequest request, HttpServletRequest request2){
        long memberid = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Review review= new Review();
        review.setTexts(request.getText());
        review.setReviewDate(LocalDateTime.now());
        review.setReviewStar(request.getReviewStar());
        review.setMember(memberService.findById(memberid));
        review.setFood(foodService.findOne(foodid));
        Long join = reviewService.join(review);

        foodService.rateUp(foodService.findId(foodid),Float.valueOf(request.getReviewStar().ordinal())+1);
        return new CreateReviewResponse(join);

    }


    /**
     사업자 리뷰답변등록
     */
    @PatchMapping("/review/{id}")
    public ReText reText(@PathVariable("id")Long id,@RequestBody @Valid ReText request){
        reviewService.update(id,null, request.getReText());
        return new ReText(request.getReText());
    }
    /**
     리뷰삭제
     */
    @DeleteMapping("/review/{id}")
    public DeleteReviewResponse deleteFoodResponse(
                @PathVariable("id")Long id, HttpServletRequest request2
    ){
            long memberid = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
            if(reviewService.findOne(id).getMember().getId()==memberid) {
                reviewService.deleteReview(id);
                return new DeleteReviewResponse(id);
            }
            else
                throw new IllegalArgumentException("등록한 리뷰가아닙니다");

    }
    @PostMapping("/user/review/{reviewid}/photos")
    public ResponseEntity upload2(@PathVariable("reviewid") Long reviewid,@RequestPart List<MultipartFile> files,HttpServletRequest request2) throws IOException {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        List<Long> list = new ArrayList<>();
        if(reviewService.findOne(reviewid).getMember().equals(memberService.findById(userPk))) {
            for (MultipartFile file : files){
                list.add(photoService.joinr(file, reviewService.findOne(reviewid)));
            }

            return new ResponseEntity(list,HttpStatus.OK);
        }
        else
            throw  new AccessDeniedException("403 return");
    }
    @DeleteMapping("/user/review/{reviewid}/delete/photos/{photoIds}")
    public ResponseEntity unloads(@PathVariable("reviewid") Long reviewid,@PathVariable List<Long> photoIds,HttpServletRequest request2) throws IOException {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        if(reviewService.findOne(reviewid).getMember().equals(memberService.findById(userPk))) {
            for (Long photoId : photoIds) {
                photoService.deletePhoto(photoRepository.findById(photoId).get());
            }
            return new ResponseEntity(HttpStatus.OK);
        }
        else
            throw  new AccessDeniedException("403 return");
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
    @NoArgsConstructor
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
            text=review.getTexts();
            retext=review.getRetext();
            reviewDate=review.getReviewDate();
            update=review.getUpdateDate();
            reviewStar=review.getReviewStar();
            food=review.getFood().getName();
            member=review.getMember().getNickName();
        }
    }






}
