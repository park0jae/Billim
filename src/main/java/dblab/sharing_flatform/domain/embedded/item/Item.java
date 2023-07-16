package dblab.sharing_flatform.domain.embedded.item;

import lombok.*;
import javax.persistence.*;

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

}
