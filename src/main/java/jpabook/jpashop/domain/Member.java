package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "memeber_id")
    private Long id;

    private String name;

    @Embedded   //내장타입이니까 어노테이션 필요
    private Address address;

    @OneToMany(mappedBy = "member")     //Order 객체의 member 변수
    private List<Order> orders = new ArrayList<>();

}
