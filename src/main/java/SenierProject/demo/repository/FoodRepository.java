package SenierProject.demo.repository;

import SenierProject.demo.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class FoodRepository {
    @PersistenceContext
    private final EntityManager em;
    //저장
    public void save(Food food){em.persist(food);}
    //단건조회
    public Food findOne(Long id){return em.find(Food.class,id);}
    //전부조회
    public List<Food> findAll(){
        return em.createQuery("select f from Food f", Food.class)
                .getResultList();
    }
    //조회
    public Food findIdWithStore(Long id){
        return em.createQuery(
                        "select f from Food f" +
                                " join fetch f.store s"+
                                " where f.id = :id"
                        ,Food.class
                ).setParameter("id",id)
                .getSingleResult();
    }
    //삭제
    public void deleteFood(Long foodId){
        em.remove(findOne(foodId));
    }
}
