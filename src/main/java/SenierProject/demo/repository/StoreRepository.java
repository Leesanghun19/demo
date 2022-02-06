package SenierProject.demo.repository;

import SenierProject.demo.domain.Member;
import SenierProject.demo.domain.Review;
import SenierProject.demo.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreRepository {
    private final EntityManager em;
    //저장
    public void save(Store store) {
        em.persist(store);
    }
    //단건조회
    public Optional<Store> findOne(Long id){
        Store store = em.find(Store.class, id);
        return Optional.ofNullable(store);}
    //음식점이름조회
    public Store findByName(String name){
        return em.createQuery("select s from Store s where s.name = :name",Store.class)
                .setParameter("name",name).getSingleResult();

    }
    //음식점 전부조회
    public List<Store> findAll(){return em.createQuery("select s from Store s",Store.class).getResultList();}
    //삭제
    public void deleteStore(Long storeId){
        em.remove(findOne(storeId).get());
    }
}
