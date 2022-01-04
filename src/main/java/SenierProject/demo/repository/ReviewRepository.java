package SenierProject.demo.repository;

import SenierProject.demo.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class ReviewRepository {
    private final EntityManager em;
    //저장
    public void save(Review review) {
        em.persist(review);
    }
    //단건조회
    public Optional<Review> findById(Long id){
        Review review = em.find(Review.class, id);
        return Optional.ofNullable(review);
    }
    //조회
    public Optional<Review> findIdWithStore(Long id){
        Review review = em.createQuery(
                        "select r from Review r" +
                                " join fetch r.store s" +
                                " where r.id = :id"
                        , Review.class
                ).setParameter("id", id)
                .getSingleResult();
        return Optional.ofNullable(review);
    }
    //조회
    public Optional<Review> findIdWithMember(Long id){
        Review review = em.createQuery(
                        "select r from Review r" +
                                " join fetch r.member m"+
                                " where r.id = :id"
                        ,Review.class
                ).setParameter("id",id)
                .getSingleResult();
        return Optional.ofNullable(review);
    }
    //삭제
    public void deleteReview(Long reviewId){
        em.remove(findById(reviewId));
    }

}
