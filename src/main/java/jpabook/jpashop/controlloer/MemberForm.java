package jpabook.jpashop.controlloer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.") //스프링에서 알아서 유효성검증 해줌
    private String name;

    private String city;
    private String street;
    private String zipcode;

}
