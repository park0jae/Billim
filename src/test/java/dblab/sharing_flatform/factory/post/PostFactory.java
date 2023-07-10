package dblab.sharing_flatform.factory.post;

import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.image.Image;
import dblab.sharing_flatform.domain.post.Post;

import java.util.List;

import static dblab.sharing_flatform.factory.category.CategoryFactory.createCategory;
import static dblab.sharing_flatform.factory.item.ItemFactory.createItem;
import static dblab.sharing_flatform.factory.member.MemberFactory.createMember;

public class PostFactory {
    public static Post createPost() {
        return new Post("title", "content", createCategory(), createItem(), List.of(), createMember());
    }
    public static Post createPost(List<Image> images) {
        return new Post("title", "content", createCategory(), createItem(), images, createMember());
    }


}
