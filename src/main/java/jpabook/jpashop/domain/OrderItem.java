package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //이 어노테이션 써주면 protected 기본 생성자 안 쳐도 된다
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private  Order order;

    private int orderPrice; //주문가격(당시)
    private int count;  //주문 수량량

    /**
     * protected 로 기본 생성자 만들어 주는 이유 : 생성 메서드를 만들어 놨는데
     * OrderItem orderItem = new OrderItem();
     * orderItem.set....()
     * 이런식으로 만들면 유지보수 하기가 어렵다
     * ==> protected로 생성자 만들어 놓으면 orderItem 생성자 호출 부분에서 오류 발생하여 제약 시킬 수 있음
     * ==> @NoArgsConstuctor(access = AccessLevel.PROTECTED) 어노테이션으로 대체 가능
     * !!!코드를 제약하는 스타일로 짜는 습관을 들이자!!! 유지보수에 유리
    protected OrderItem() {
    }

     */

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancle() {
        getItem().addStock(count);
    }

    //==조회 로직==//
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
