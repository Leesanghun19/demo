package SenierProject.demo.repository;

import SenierProject.demo.domain.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class FoodRepository {
    @PersistenceContext
    private final EntityManager em;
    //저장
    public void save(Food food){em.persist(food);}
    //단건조회
    public Optional<Food> findById(Long id){Food food=em.find(Food.class,id);return Optional.ofNullable(food);}
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
    public long count(){
        return em.createQuery("select count(f) from Food f",Long.class).getSingleResult();
    }
    //삭제
    public void deleteFood(Long foodId){
        em.remove(findById(foodId));
    }
}
