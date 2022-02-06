package SenierProject.demo.Service;

import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.Member;
import SenierProject.demo.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;
    @PostConstruct
    public void init() {
        initService.dbInit();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void dbInit(){
            Store store =createStore("샘플가게","가게소게","010-2222-4444","원산지","사업자등록번호");
            em.persist(store);
            Food food=food(store,"셈플음식",10000L);
            store.getFood().add(food);
            em.persist(food);
            em.flush();

        }
        public void dbInit2(){
            Store store =createStore("샘플가게2","가게소게2","010-2222-4444","원산지2","사업자등록번호2");
            em.persist(store);
            Food food=food(store,"셈플음식2",10000L);
            store.getFood().add(food);
            em.persist(food);
            em.flush();

        }
        private Store createStore(String name,String introduce,String phoneNumber,String foodOrigin,String number){
            Store store = new Store();
            store.setName(name);
            store.setPhoneNumber(phoneNumber);
            store.setNumber(number);
            store.setIntroduce(introduce);
            store.setFoodOrigin(foodOrigin);
            return store;
        }
        private Food food(Store store,String name,Long price){
            Food food = new Food();
            food.setStore(store);
            food.setName(name);
            food.setPrice(price);
            return food;
        }
    }
}
