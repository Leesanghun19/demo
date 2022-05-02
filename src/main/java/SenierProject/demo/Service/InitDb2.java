package SenierProject.demo.Service;

import SenierProject.demo.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb2 {
    private final InitService initService;

    @PostConstruct
    public void init(){
      //initService.dbInit();
      //initService.dbInit2();
      //initService.dbInit3();
      //initService.dbInit4();
      //initService.dbInit5();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Value("${id}")
        private String id;
        @Value("${ps}")
        private String ps;
        public void dbInit() {
            Member member = new Member("닉1","비번1","이메일1");
            //member.setId(999L);
            em.persist(member);

            em.flush();
        }
        public void dbInit2() {
            Member member = new Member("닉2","비번2","이메일2");

            em.persist(member);

            em.flush();
        }
        public void dbInit3() {
            Member member = new Member("string",passwordEncoder.encode("string"),"string");
            Email email = new Email("string","1234");

            em.persist(member);
            em.persist(email);

            em.flush();
        }
        public void dbInit4() {
            Member member = new Member(id,passwordEncoder.encode(ps),"root");
            member.getRoles().add("ROLE_ADMIN");

            em.persist(member);

            em.flush();
        }
        //사람추가
        public void dbInit5() {

            Business business = new Business("비지니스",passwordEncoder.encode("1234"),"business");

            em.persist(business);


            Store store =createStore("비지니스가게","비지니스소게","010-2222-4444","원산지","사업자등록번호");
            em.persist(store);
            Food food=food(store,"셈플음식a","10000");
            food.setText("샘플 음식 설명");
            food.setRateNum(2L);
            food.setRateAverage(4.5F);
            Food food2=food(store,"셈플음식b","20000");
            Food food3=food(store,"셈플음식c","30000");
            store.getFood().add(food);
            store.getFood().add(food2);
            store.getFood().add(food3);
            store.setBusiness(business);
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
        private Food food(Store store,String name,String price){
            Food food = new Food();
            food.setStore(store);
            food.setName(name);
            food.setPrice(price);
            return food;
        }
        }

}
