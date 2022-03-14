package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)  //EnumType.ORDINAL -> 순서대로 숫자로 메김(사용XXXXXXXXX) => 값 사이에 하나가 추가되면 순번이 밀림
    //꼭 EnumType.STRING 으로 사용하자
    private DeliveryStatus status;  //READY, COMP

}
