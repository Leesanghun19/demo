package SenierProject.demo.controller;

import SenierProject.demo.Service.FoodService;
import SenierProject.demo.Service.MemberService;
import SenierProject.demo.Service.PhotoService;
import SenierProject.demo.Service.StoreService;
import SenierProject.demo.domain.*;
import SenierProject.demo.jwt.JwtTokenProvider;
import SenierProject.demo.repository.BusinessRepository;
import SenierProject.demo.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final StoreService storeService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;
    private final BusinessRepository businessRepository;
    /**
     음식조회
     */
    @GetMapping("/foods/{id}")
    public Result food(
            @PathVariable("id")Long id
    ){
        Food food = foodService.findId(id);
        FoodDto foodDto= new FoodDto(food);
        return new Result(foodDto);
    }
    /**
     음식등록
     */
    @PostMapping("/food")
    public CreateFoodResponse saveFood( @RequestBody @Valid CreateFoodRequest request, HttpServletRequest request2) throws AccessDeniedException {
        long businessPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
            Food food = Food.createFood(storeService.findOne(businessRepository.findById(businessPk).get().getStore().getId()), request.getName(), request.getPrice(), request.getText());
            Long id = foodService.join(food);
            return new CreateFoodResponse(id);


    }

    /**
     음식수정
     */
    @PatchMapping("/food/{id}")
    public UpdateFoodResponse updateFood(
            @PathVariable("id")Long id,
            @RequestBody  @Valid UpdateFoodRequest request,HttpServletRequest request2
    ) throws AccessDeniedException {
        long businessPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        if(foodService.findOne(id).getStore()==businessRepository.findById(businessPk).get().getStore()) {
            foodService.update(id, request.getName(), request.getPrice());
            Food food=foodService.findId(id);
            return new UpdateFoodResponse(food.getName(), food.getPrice());
        }
        else
            throw new AccessDeniedException("403 return");

    }
    /**
     품절
     */
    @PatchMapping("/food/{id}/soldout")
    public saleFood soldOut(
            @PathVariable("id")Long id,HttpServletRequest request2
    ) throws AccessDeniedException {
        long businessPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        if(foodService.findOne(id).getStore()==businessRepository.findById(businessPk).get().getStore()) {
            foodService.sale(id,FoodStatus.SOLDOUT);
            Food food = foodService.findId(id);
            return new saleFood(food.getStatus());
        }
        else
            throw new AccessDeniedException("403 return");

    }
    /**
     판매중
     */
    @PatchMapping("/food/{id}/onsale")
    public saleFood onSale(
            @PathVariable("id")Long id,HttpServletRequest request2
    ) throws AccessDeniedException {

        long businessPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        if(foodService.findOne(id).getStore()==businessRepository.findById(businessPk).get().getStore()) {
            foodService.sale(id,FoodStatus.ONSALE);
            Food food = foodService.findId(id);
            return new saleFood(food.getStatus());
        }
        else
            throw new AccessDeniedException("403 return");
    }
    /**
     음식삭제
     */
    @DeleteMapping("/food/{id}")
    public DeleteFoodResponse deleteFoodResponse(
            @PathVariable("id")Long id,HttpServletRequest request2
    ) throws AccessDeniedException {

        long businessPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        if(foodService.findOne(id).getStore().equals(businessRepository.findById(businessPk).get().getStore())) {
            foodService.deleteFood(id);
            return new DeleteFoodResponse(id);
        }
        else
            throw new AccessDeniedException("403 return");
    }
    @PostMapping("/user/store/{foodid}/food")
    public ResponseEntity upload(@PathVariable("foodid") Long id, @RequestPart MultipartFile file, HttpServletRequest request2) throws IOException {
        if(foodService.findOne(id).getPhoto()!=null){
            throw new IllegalStateException("이미 사진이있습니다");
        }

            Long pid = photoService.joinf(file, foodService.findOne(id));
            return new ResponseEntity(pid, HttpStatus.OK);


    }

    /**
     DTO
     */

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    @AllArgsConstructor
    private class FoodDto{
        private Long id;
        private String name;
        private String text;
        private String price;
        private FoodStatus status;
        private String storeName;
        private Float rateAverage;
        private Long rateNum;
        private Long photoId;
        private List<ReviewList> reviewList;
        public FoodDto(Food food){
            id=food.getId();
            name=food.getName();
            text=food.getText();
            price= food.getPrice();
            status=food.getStatus();
            storeName=food.getStore().getName();
            rateAverage=food.getRateAverage();
            rateNum=food.getRateNum();
            if(!(food.getPhoto()==null)) {
                photoId = food.getPhoto().getId();
            }
            reviewList=food.getReviews().stream()
                    .map(review->new ReviewList(review))
                    .collect(Collectors.toList());
        }
    }
    @Data
    private class ReviewList{
        private String text;
        private String retext;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private int star;
        private String nickname;
        private ArrayList<Long> photoIds= new ArrayList<>();
        private Long reviewid;
        public  ReviewList(Review review){
            reviewid=review.getId();
            text=review.getTexts();
            retext=review.getRetext();
            createTime=review.getReviewDate();
            updateTime=review.getUpdateDate();
            star=review.getReviewStar().ordinal()+1;
            nickname=review.getMember().getNickName();
            if((review.getPhotos()!=null)) {
                for (Photo photo : review.getPhotos()) {
                     Long a=photo.getId();
                     photoIds.add(a);
                }
            }

        }
    }
    @Data
    static class CreateFoodResponse{
        private Long id;

        public CreateFoodResponse(Long id) {
            this.id = id;
        }
    }
    @Data
    static class CreateFoodRequest {
        @NotEmpty
        private String name;
        private String price;
        private String storeName;
        private String text;
    }
    @Data
    @AllArgsConstructor
    static class UpdateFoodResponse{
        private String name;
        private String price;

    }
    @Data

    static class UpdateFoodRequest{
        private String name;
        private String price;

    }
    @Data
    @AllArgsConstructor
    static class saleFood{
        private FoodStatus status;
    }
    @Data
    @AllArgsConstructor
    static class DeleteFoodResponse{
        private Long id;

    }
}
