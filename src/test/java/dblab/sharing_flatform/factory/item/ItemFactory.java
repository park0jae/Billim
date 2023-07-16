package dblab.sharing_flatform.factory.item;

import dblab.sharing_flatform.domain.embedded.item.Item;

public class ItemFactory {
    public static Item createItem() {
        return new Item("name", 1000L, 1L);
    }
}
