package jpabook.jpashop.controlloer;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    //BindingResult가 있으면 오류가 있을때 바로 팅기지 않고 BindingResult에 오류가 담겨서 실행이 된다
    //Member 엔티티를 쓰지 않는 이유 : 엔티티와 폼이 완벽히 맞지 않는다. 엔티티가 지저분해진다.
    //Form으로 필요한 데이터만 정제해서 쓰는걸 권장함!!
    public String create(@Valid MemberForm form, BindingResult result) { //@Valid 어노테이션 쓰면 객체에 @NoEmpty 같은 validation 기능을 해줌

        if (result.hasErrors()) { //result에 에러가 생기면 다시 createMemberForm으로 감
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
