package dblab.sharing_platform.factory.item;

import dblab.sharing_platform.domain.embedded.item.Item;

public class ItemFactory {
    public static Item createItem() {
        return new Item("name", 1000L, 1L);
    }
}
