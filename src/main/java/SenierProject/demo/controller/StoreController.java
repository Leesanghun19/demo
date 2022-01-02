package SenierProject.demo.controller;

import SenierProject.demo.Service.StoreService;
import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.FoodStatus;
import SenierProject.demo.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

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
    /**
     등록
     */
    @PostMapping("/store")
    public CreateStoreResponse saveStore(@RequestBody @Valid CreateStoreRequest request){
        Store store= new Store();
        store.setName(request.getName());
        Long id = storeService.join(store);
        return new CreateStoreResponse(id);
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
     */
    @DeleteMapping("/store/{id}")
    public DeleteFoodResponse deleteFoodResponse(
            @PathVariable("id")Long id
    ){
        storeService.deleteStore(id);
        return new DeleteFoodResponse(id);
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
    static class StoreDto {
        private String name;
        public StoreDto(Store store){
            name=store.getName();
        }
    }
    @Data
    @AllArgsConstructor
    static class StoreDtoFood{
        private String name;
        private List<FoodListDto> foodListDtoList;
        public StoreDtoFood(Store store){
            name=store.getName();
            foodListDtoList=store.getFood().stream()
                    .map(food -> new FoodListDto(food)).collect(Collectors.toList());
        }
    }
    @Getter
    static class FoodListDto{
        private Long id;
        private String name;
        private Long price;
        private FoodStatus status;
        public FoodListDto(Food food){
            id=food.getId();
            name= food.getName();
            price=food.getPrice();
            status=food.getStatus();
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
