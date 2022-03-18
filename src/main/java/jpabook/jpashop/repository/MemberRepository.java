package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //@PersistenceContext
    //@Autowired 원래 EntityManager는 무조건 @PersistenceContext 표준 어노테이션으로 인젝션을 해주는데
    //           Spring data jpa가 @Autowired 지원해줌 -> Autowired가 되니깐 lombok-@RequiredArgsConstructor 어노테이션 사용가능
    //           -> 코드 간결하게 작성 가능(@RequiredArgsConstructor 쓰려면 final 변수 선언!!!)
    //           MemberService.java 참고
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        //createQuery(JPQL넣으면 됨, 반환 타입)
        //JPQL은 SQL이랑 조금씩 차이가 있음 : SQL은 테이블을 대상으로 쿼리, JPQL은 Entity 객체를 대상으로 쿼리
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
