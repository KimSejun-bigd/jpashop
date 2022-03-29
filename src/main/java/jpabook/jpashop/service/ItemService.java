package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional //클래스에 readOnly true 걸었으니 여기다도 걸어줘야 저장됨
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //findOne으로 찾아온 findItem은 영속성이다.
    //findItem에 값들을 세팅해주고 그냥 커밋만 해주면 영속성컨텍스트에 의해 변경된 컬럼을 감지해서 자동으로 update쿼리 날려주ㅗㅓㅁ
    //--> 변경감지 사용
    //병합(merge)는 파라미터 들어온대로 통채로 업데이트 침. null로도 업데이타 가능
    //병합 사용을 지양하자...!! 변경감지를 쓰자!!
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItem() { return itemRepository.findAll(); }

    public Item findOne(Long itemId) { return itemRepository.findOne(itemId); }
}
