package dblab.sharing_platform.domain.embedded.item;

import dblab.sharing_platform.dto.item.ItemCreateRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    private String name;

    private Long price;

    private Long quantity;

    public void updateItem(String name, Long price, Long quantity) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public static Item toEntity(ItemCreateRequestDto itemCreateRequestDto) {
        return new Item(itemCreateRequestDto.getName(),
                itemCreateRequestDto.getPrice(),
                itemCreateRequestDto.getQuantity());
    }

}
