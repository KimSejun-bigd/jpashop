package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)  // XTOOne ( OneToOne, ManyToOne) 에서는 디폴트가 EAGER(즉시로딩), 무조건 지연로딩(LAZY)로 설정해줘야함!!!!! => JPQL에서 N+1 문제의 주범
                                        // XToMany (OneToMany, ManyToMany) 는 디폴트가 LAZY임.
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //양방향 관계일때 꼭 주인명시 주인이 아닌 항목에 대해 mappedBy로 주인 객체의 변수 명시
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)    //1대1 관계에서는 FK를 액세스가 많이 일어나는 곳에 넣는것이 좋은듯
    //이 시스템에서는 오더를 보면서 딜리버리를 본다고 가정한 것. 딜리버리를 가지고 오더를 찾을 일은 거의 없다고 가정.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;       //자바8에서는 LocalDateTime 쓰면 어노테이션 없이 알아서 해줌

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //==연관관계 메서드==//
    //양방향 관계일때 양쪽 세팅을 한 코드로 해주기 위해
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    //Order만 생성해야 할게 아니라 OrderItem, Delivery 같은 연계관계 들어간 객체도 생성해야 함 -> 별도의 생성 메서드가 있으면 좋음(한번에 객체 생성 가능)
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    //엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것은 "도메인 모델 패턴" 이라고 한다.
    //==> 서비스 계층을 단순히 엔티티에 필요한 요청을 위임하는 역할만 하도록
    // <--> 반대로 서비스에서 비즈니스 로직을 처리하는 (ex.일반적으로 SQL을 다룰떄와 같이 했던것들..) "트랜잭션 스크립트 패턴"
    /**
     * 주문 취소
     */
    public void cancle() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCLE);
        for (OrderItem orderItem : orderItems){
            orderItem.cancle();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
