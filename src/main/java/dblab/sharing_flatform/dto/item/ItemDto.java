package dblab.sharing_flatform.dto.item;

import dblab.sharing_flatform.domain.item.Item;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDto {

    private String name;

    private Long price;

    private Long quantity;

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getName(),
                item.getPrice(),
                item.getQuantity());
    }
}
