package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //readOnly=true 를 클래스에 걸면 개별적으로 transactionan() 걸어주지 않은 곳에 다 걸림
//@AllArgsConstructor
@RequiredArgsConstructor // @AllArgsConstructor 와 달리 'final'이 있는 필드만 생성자를 만들어줌
public class MemberService {

    //final로 해 놓으면 생성자 값을 만들때 오류가 나더라도 확인하기 쉬움 - final 넣는 걸 권장
    private final MemberRepository memberRepository;

    /* 최신버전 스프링에서는 생성자가 하나 있을경우에 자동으로 해당 생성자에 Autowried 어노테이션을 걸어줌
       lombok에 @AllArgsConstructor 어노테이션을 쓰면 '모든' 필드에 대한 생성자를 만들어줌
        -> 가장 베스트인 방법은 @RequiredArgsConstructor으로 final 필드만 생성자를 자동으로 만들어주는게 좋음
           아래의 코드(MemberService 생성자) 작성할 필요가 없어짐
    @Autowired  //생성자 인젝션 - Setter인젝션보다 생성자 인젝션을 쓰도록
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    */

    /**
     * 회원가입
     */
    @Transactional //내부에 개별적으로 Transactional 걸어주면 우선권을 갖음(readOnly=false가 디폺트)
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
