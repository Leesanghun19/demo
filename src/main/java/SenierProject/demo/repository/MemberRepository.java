package SenierProject.demo.repository;

import SenierProject.demo.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class MemberRepository {

    @PersistenceContext
    private final EntityManager em;
    //저장
    public void save(Member member){em.persist(member);}
    //단건조회
    public Member findOne(Long id){return em.find(Member.class,id);}
    //전부조회
    public List<Member> findALl(){
        return em.createQuery("select m from Member m",Member.class).getResultList();
    }
    //이메일조회
    public List<Member> findByEmail(String email){
        return em.createQuery("select m from Member m where m.email = :email",Member.class)
                .setParameter("email",email)
                .getResultList();
    }
    //삭제
    public void deleteMember(Long memberId){
        em.remove(findOne(memberId));
    }
}
