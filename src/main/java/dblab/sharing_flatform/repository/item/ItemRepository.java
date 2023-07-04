package dblab.sharing_flatform.repository.item;

import dblab.sharing_flatform.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
