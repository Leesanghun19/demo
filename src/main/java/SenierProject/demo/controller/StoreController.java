package SenierProject.demo.controller;

import SenierProject.demo.Service.PhotoService;
import SenierProject.demo.Service.StoreService;
import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.FoodStatus;
import SenierProject.demo.domain.Store;
import SenierProject.demo.jwt.JwtTokenProvider;
import SenierProject.demo.repository.BusinessRepository;
import SenierProject.demo.repository.MemberRepository;
import SenierProject.demo.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final BusinessRepository businessRepository;
    private final PhotoService photoService;
    private final PhotoRepository photoRepository;
    /**
     전체조회
     */
    @GetMapping("/stores")
    public Result storeAll(){
        List<Store> stores=storeService.findALl();
        List<StoreDto> collect = stores.stream()
                .map(o->new StoreDto(o)).collect(Collectors.toList());
        return new Result(collect);
    }
    /**
     단건조회
     */
    @GetMapping("/store/{id}")
    public Result store(
            @PathVariable("id")Long id
    ){
        Store store=storeService.findOne(id);
        StoreDtoFood storeDtoFood=new StoreDtoFood(store);
        return new Result(storeDtoFood);
    }
    @GetMapping("/store")
    public Result storeForBusiness(
            HttpServletRequest request2
    ){
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Store store=storeService.findOne(businessRepository.findById(userPk).get().getStore().getId());
        StoreDtoFood storeDtoFood=new StoreDtoFood(store);
        return new Result(storeDtoFood);
    }
    /**
     등록

    @PostMapping("/store")
    public CreateStoreResponse saveStore(@RequestBody @Valid CreateStoreRequest request, HttpServletRequest request2){
        long business = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Store store= new Store();
        store.setName(request.getName());
        store.setIntroduce(request.introduce);
        store.setFoodOrigin(request.foodOrigin);
        store.setNumber(request.number);
        store.setPhoneNumber(request.phoneNumber);
        Long id = storeService.join(store);
        return new CreateStoreResponse(id);
    }

     */
    /**
     *사진등록
     */
    @PostMapping("/user/store/photo")
    public ResponseEntity upload( @RequestPart MultipartFile file, HttpServletRequest request2) throws IOException {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        if(businessRepository.findById(userPk).get().getStore().getPhoto()!=null){
            throw new IllegalStateException("이미 사진이있습니다");
        }
        Long id = businessRepository.findById(userPk).get().getStore().getId();
            Long pid = photoService.joins(file, storeService.findOne(id));
            return new ResponseEntity(pid, HttpStatus.OK);


    }
    @DeleteMapping("/user/store/{storeId}/delete/photos/{photoIds}")
    public ResponseEntity unloads(@PathVariable("photoIds") List<Long> photoIds,HttpServletRequest request2) throws IOException {
        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Long storeId = businessRepository.findById(userPk).get().getStore().getId();

            for (Long photoId : photoIds) {
                photoService.deletePhoto(photoRepository.findById(photoId).get());
            }
            return new ResponseEntity(HttpStatus.OK);


    }
    /**
     이름찾기
     */

    @GetMapping("/storeName/{name}")
    public Result storeName(
            @PathVariable("name")String name
    ){
        Store store = storeService.findName(name);
        StoreName storeName=new StoreName(store.getId(),store.getName());
        return new Result(storeName);
    }
    /**
     삭제

    @DeleteMapping("/store/{id}")
    public DeleteFoodResponse deleteFoodResponse(
            @PathVariable("id")Long id
    ){
        storeService.deleteStore(id);
        return new DeleteFoodResponse(id);
    }
     */
    /**
     DTO
     */

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    static class StoreDto {
        private Long id;
        private String name;
        private String introduce;
        private Long photoId;
        public StoreDto(Store store){
            id= store.getId();
            name=store.getName();
            introduce=store.getIntroduce();


            if(!(store.getPhoto()==null)) {
                photoId = store.getPhoto().getId();
            }

        }
    }
    @Data
    @AllArgsConstructor
    static class StoreDtoFood{
        private Long StoreId;
        private String name;
        private List<FoodListDto> foodListDtoList;
        private String phoneNumber;
        private String foodOrigin;//원산지
        public StoreDtoFood(Store store){
            StoreId=store.getId();
            name=store.getName();
            phoneNumber= store.getPhoneNumber();
            foodOrigin=store.getFoodOrigin();
            foodListDtoList=store.getFood().stream()
                    .map(food -> new FoodListDto(food)).collect(Collectors.toList());
        }
    }
    @Getter
    static class FoodListDto{
        private Long id;
        private String name;
        private String price;
        private FoodStatus status;
        private Long photoId;
        public FoodListDto(Food food){
            id=food.getId();
            name= food.getName();
            price=food.getPrice();
            status=food.getStatus();
            if(!(food.getPhoto()==null)) {
                photoId = food.getPhoto().getId();
            }
        }
    }
    @Data
    static class CreateStoreResponse {
        private Long id;

        public CreateStoreResponse(Long id) {
            this.id = id;
        }
    }
    @Data
    static class CreateStoreRequest{
        @NotEmpty
        private String name;
        private String phoneNumber;
        private String number;
        private String introduce;
        private String foodOrigin;

    }
    @Data
    @AllArgsConstructor
    static class DeleteFoodResponse{
        private Long id;

    }
    @Data
    @AllArgsConstructor
    static class StoreName{
        private Long id;
        private String name;
    }
}
