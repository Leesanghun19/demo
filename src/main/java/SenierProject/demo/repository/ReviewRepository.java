package SenierProject.demo.repository;

import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
@Repository
@RequiredArgsConstructor

public class ReviewRepository {
    private final EntityManager em;
    //저장
    public void save(Review review) {
        em.persist(review);
    }
    //단건조회
    public Review findOne(Long id){return em.find(Review.class,id);}
    //조회
    public Review findIdWithStore(Long id){
        return em.createQuery(
                        "select r from Review r" +
                                " join fetch r.store s"+
                                " where r.id = :id"
                        ,Review.class
                ).setParameter("id",id)
                .getSingleResult();
    }
    //조회
    public Review findIdWithMember(Long id){
        return em.createQuery(
                        "select r from Review r" +
                                " join fetch r.member m"+
                                " where r.id = :id"
                        ,Review.class
                ).setParameter("id",id)
                .getSingleResult();
    }
    //삭제
    public void deleteReview(Long reviewId){
        em.remove(findOne(reviewId));
    }

}
