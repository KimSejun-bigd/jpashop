package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속 전략 -> 한 테이블에 모든 테이블 때려박는 전략
@DiscriminatorColumn(name = "dtype") //구분값 dtype
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity; //재고수량을 엔티티 안에 가지고 있으므로 수량 + - 하는 비즈니스 로직을 엔티티 안에 만드는게 좋음
                               //Setter 쓰지말고 비즈니스 로직을 써서 수량 변경해라(더 객체지향적 -> 응집력이 높음)

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    //재고를 늘리고 줄이는거(엔티티 자체가 해결할 수 있는 것들은 엔티티 안에 로직을 넣는게 좋음)
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
