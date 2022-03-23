package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        //Order 엔티티에서  orderItems, delivery 객체에 cascade = ALL 해줬기 때문에 order를 persist해주면 orderItems, delivery도 자동으로 persist 해줌
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancleOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancle();
        /**
         * JPA의 강점!!
         * mybatis 같이 직접 쿼리 짜야 하는경우 직접 수량, 주문 상태 등을 변경해주는 update 쿼리를 또 짜서 날려야함
         * JPA는 엔티티에서 변경포인트를 감지해서 변경내역을 자동으로 DB에 업데이트 해줌(자동으로 update 쿼리 날려줌)
         */
    }

    //검색
/*

    public List<Order> findOrders(OrserSearch orserSearch) {
        return orderRepository.findAll(orderSerch);
    }
*/

}
