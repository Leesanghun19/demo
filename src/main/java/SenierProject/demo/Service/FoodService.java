package SenierProject.demo.Service;

import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.FoodStatus;
import SenierProject.demo.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    //음식등록
    @Transactional
    public Long join(Food food){
        foodRepository.save(food);
        return  food.getId();
    }
    //수정
    @Transactional
    public void update(Long id,String name,Long price){
        Food food = foodRepository.findById(id).get();
        food.setName(name);
        food.setPrice(price);
        food.setUpdate(LocalDateTime.now());
    }
    @Transactional
    public void sale(Long id, FoodStatus status){
        Food food = foodRepository.findById(id).get();
        food.setStatus(status);
    }
    //조회
    public Food findOne(Long foodId){return foodRepository.findById(foodId).get();}
    //public List<Food> findAllFood(){return foodRepository.findAll();}
    public Food findId(Long foodId){return foodRepository.findIdWithStore(foodId);}
    //삭제
    @Transactional
    public void deleteFood(Long foodId){foodRepository.deleteFood(foodId);}
}
