package SenierProject.demo.Service;

import SenierProject.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        //initService.dbInit();
       //initService.dbInit2();


    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        @Autowired
        private PasswordEncoder passwordEncoder;

        public void dbInit(){
            Store store =createStore("샘플가게","가게소게","010-2222-4444","원산지","사업자등록번호");
            em.persist(store);
            Food food=food(store,"셈플음식1-1","10000");
            food.setText("샘플 음식 설명");
            food.setRateNum(2L);
            food.setRateAverage(4.5F);
            Food food2=food(store,"셈플음식1-2","20000");
            Food food3=food(store,"셈플음식1-3","30000");
            store.getFood().add(food);
            store.getFood().add(food2);
            store.getFood().add(food3);
            Member member=member("사람1","01031277412","aaaa@aaaa.com");
            Review review=review(food,member);
            Review review1=review2(food,member);
            review.setFood(food);

            em.persist(member);
            em.persist(review);
            em.persist(review1);
            em.persist(food);
            em.persist(food2);
            em.persist(food3);
            em.flush();

        }
        public void dbInit2(){
            Store store =createStore("샘플가게2","가게소게2","010-2222-4444","원산지2","사업자등록번호2");
            em.persist(store);
            Food food=food(store,"셈플음식2-1","10000");
            Food food2=food(store,"셈플음식2-2","20000");
            Food food3=food(store,"셈플음식2-3","30000");
            store.getFood().add(food);
            store.getFood().add(food2);
            store.getFood().add(food3);
            em.persist(food);
            em.persist(food2);
            em.persist(food3);
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
        private Member member(String nickname, String phoneNumber, String email){
            Member member= new Member("","","");
            member.setNickName(nickname);
            member.setPhoneNumber(phoneNumber);
            member.setEmail(email);
            //String ps=passwordEncoder.encode("string");
            member.setPassword(passwordEncoder.encode("ps"));

            return member;
        }
        private Review review(Food food,Member member){
            Review review= new Review();
            review.setFood(food);
            review.setMember(member);
            review.setTexts("샘플리뷰");
            review.setReviewStar(ReviewStar.FOUR);
            return review;
        }
        private Review review2(Food food,Member member){
            Review review= new Review();
            review.setFood(food);
            review.setMember(member);
            review.setTexts("샘플리뷰");
            review.setRetext("샘플답변");
            review.setReviewStar(ReviewStar.FIVE);
            return review;
        }
        private Food food(Store store,String name,String price){
            Food food = new Food();
            food.setStore(store);
            food.setName(name);
            food.setPrice(price);
            return food;
        }

    }
}
