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
        /**
      initService.dbInit();
      initService.dbInit2();
      initService.dbInit3();
      initService.dbInit4();
      initService.dbInit4_1();
      initService.dbInit5();
      initService.dbInit5_2();
      initService.dbInit5_3();
         */
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
            Member member = new Member("홍길동","비번1","이메일1");
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
        public void dbInit4_1() {
            Member member = new Member(id,passwordEncoder.encode(ps),"root2");
            member.getRoles().add("ROLE_ADMIN");

            em.persist(member);

            em.flush();
        }
        //사람추가
        public void dbInit5() {

            Business business = new Business("홍길동",passwordEncoder.encode("1234"),"business");

            em.persist(business);


            Store store =createStore("인천대치킨","인천대 치킨집입니다","010-2222-4444","닭다리살 국내산, 가슴살 국내산, 텐더(닭안심) 국내산, 닭다리 국내산, 날개 국내산, 근위 국내산","사업자등록번호-01023-4253-6678");
            em.persist(store);
            Food food=food(store,"양념치킨","15000");
            food.setText("대표매뉴 양념치킨입니다");
            food.setRateNum(2L);
            food.setRateAverage(4.5F);
            Food food2=food(store,"간장치킨","20000");
            Food food3=food(store,"로제치킨","22000");
            store.getFood().add(food);
            store.getFood().add(food2);
            store.getFood().add(food3);
            store.setBusiness(business);
            em.flush();
        }
        public void dbInit5_2() {

            Business business = new Business("김철수",passwordEncoder.encode("1234"),"business2");

            em.persist(business);


            Store store =createStore("인천대덮밥","인천대 덮밥집입니다","010-4522-5544","닭다리살 국내산, 가슴살 국내산, 텐더(닭안심) 국내산, 닭다리 국내산, 날개 국내산, 근위 국내산","사업자등록번호-09993-43233-6678");
            em.persist(store);
            Food food=food(store,"양념마요덮밥","15000");
            food.setText("양념마요가 가득들어간 덮밥입니다");
            food.setRateNum(2L);
            food.setRateAverage(4.5F);
            Food food2=food(store,"치킨덮밥","20000");
            Food food3=food(store,"인천대덮밥","22000");
            store.getFood().add(food);
            store.getFood().add(food2);
            store.getFood().add(food3);
            store.setBusiness(business);
            em.flush();
        }
        public void dbInit5_3() {

            Business business = new Business("나하나",passwordEncoder.encode("1234"),"business3");

            em.persist(business);


            Store store =createStore("인천대 떡볶이","인천대 떡볶이입니다","010-9922-7744","닭다리살 국내산, 가슴살 국내산, 텐더(닭안심) 국내산, 닭다리 국내산, 날개 국내산, 근위 국내산","사업자등록번호-243423-4244-6678");
            em.persist(store);
            Food food=food(store,"오리지널 떡볶이","15000");
            food.setText("가장기본 맛이고 맵습니다");
            food.setRateNum(2L);
            food.setRateAverage(4.5F);
            Food food2=food(store,"로제떡볶이","20000");
            Food food3=food(store,"오뎅많이 떡볶이","22000");
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
