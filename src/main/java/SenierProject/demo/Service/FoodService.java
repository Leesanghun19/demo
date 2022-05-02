package SenierProject.demo.Service;

import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.FoodStatus;
import SenierProject.demo.domain.Review;
import SenierProject.demo.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    public void update(Long id,String name,String price){
        Food food = foodRepository.findById(id).get();
        food.setName(name);
        food.setPrice(price);

    }
    @Transactional
    public void sale(Long id, FoodStatus status){
        Food food = foodRepository.findById(id).get();
        food.setStatus(status);
    }

    @Transactional
    public void rateUp(Food food,Float rateStar){
        if(food.getRateAverage()==null){
            food.setRateNum(1L);
            food.setRateAverage(rateStar);
        }
        else {

            food.setRateNum(food.getRateNum()+1);
            food.setRateAverage((food.getRateAverage()*(food.getRateNum()-1)+rateStar)/food.getRateNum());
        }
    }
    //조회
    public Food findOne(Long foodId){return foodRepository.findById(foodId).get();}
    //public List<Food> findAllFood(){return foodRepository.findAll();}
    public Food findId(Long foodId){return foodRepository.findIdWithStore(foodId);}
    //삭제
    @Transactional
    public void deleteFood(Long foodId){foodRepository.deleteFood(foodId);}
}
