package jpabook.jpashop.service;

import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //JUnit 실행할때 스프링이랑 엮어서 실행하겠단 것
@SpringBootTest              //스프링 부트를 띄운 상태로 테스트하려면 써줘야함
@Transactional               //트랜잭션을 걸고 테스트 한다음에 롤백 시켜줌(디폹트가 rollback = True)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    //insert 쿼리찍히는걸 보고싶다면 Entitymanager flush() 쓰면 보임(실제로 쿼리는 날림) -> Rollback false 안하는 이상 여전히 롤백은 됨
    @Autowired EntityManager em;

    @Test
    //@Rollback(false) //Transactional이 Test케이스에 붙으면 자동으로 롤백을 시킴
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        //저장하기전 id와 저장된 id 비교
        em.flush(); //인서트를 날림 but @Rollback 어노테이션에 따라 롤백 여부정해짐
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) //expectd를 쓰면 try catch로 예외 안잡아도 된다. 코드 깔끔해짐
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다."); //여기까지 오면 잘못된 테스트이므로 fail()을 떨궈주도록 만든다.
    }


}