package SenierProject.demo.controller;

import SenierProject.demo.Service.FoodService;
import SenierProject.demo.Service.StoreService;
import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.FoodStatus;
import SenierProject.demo.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final StoreService storeService;
    /**
     음식조회
     */
    @GetMapping("/food/{id}")
    public Result food(
            @PathVariable("id")Long id
    ){
        Food food = foodService.findId(id);
        FoodDto foodDto= new FoodDto(food.getName(),food.getPrice(),food.getStatus(),food.getStore().getName());
        return new Result(foodDto);
    }
    /**
     음식등록
     */
    @PostMapping("/food/{id}")
    public CreateFoodResponse saveFood(@PathVariable("id")Long storeId,@RequestBody @Valid CreateFoodRequest request){
        Food food=Food.createFood(storeService.findOne(storeId),request.getName(),request.getPrice());
        Long id =foodService.join(food);
        return new CreateFoodResponse(id);
    }

    /**
     음식수정
     */
    @PatchMapping("/food/{id}")
    public UpdateFoodResponse updateFood(
            @PathVariable("id")Long id,
            @RequestBody  @Valid UpdateFoodRequest request
    ){
        foodService.update(id, request.getName(), request.getPrice());
        Food food=foodService.findId(id);
        return new UpdateFoodResponse(food.getName(), food.getPrice());
    }
    /**
     품절
     */
    @PatchMapping("/food/{id}/soldout")
    public saleFood soldOut(
            @PathVariable("id")Long id

    ){
        foodService.sale(id,FoodStatus.SOLDOUT);
        Food food = foodService.findId(id);
        return new saleFood(food.getStatus());
    }
    /**
     판매중
     */
    @PatchMapping("/food/{id}/onsale")
    public saleFood onSale(
            @PathVariable("id")Long id


    ){
        foodService.sale(id,FoodStatus.ONSALE);
        Food food = foodService.findId(id);
        return new saleFood(food.getStatus());
    }
    /**
     음식삭제
     */
    @DeleteMapping("/food/{id}")
    public DeleteFoodResponse deleteFoodResponse(
            @PathVariable("id")Long id
    ){
        foodService.deleteFood(id);
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
    @AllArgsConstructor
    static class FoodDto{
        private String name;
        private Long price;
        private FoodStatus status;
        private String storeName;
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
        private Long price;
        private String storeName;
    }
    @Data
    @AllArgsConstructor
    static class UpdateFoodResponse{
        private String name;
        private Long price;

    }
    @Data

    static class UpdateFoodRequest{
        private String name;
        private Long price;

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