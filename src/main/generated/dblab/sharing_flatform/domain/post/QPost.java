package dblab.sharing_flatform.domain.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 418846515L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final dblab.sharing_flatform.domain.base.QBaseTime _super = new dblab.sharing_flatform.domain.base.QBaseTime(this);

    public final dblab.sharing_flatform.domain.category.QCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdTime = _super.createdTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<dblab.sharing_flatform.domain.image.Image, dblab.sharing_flatform.domain.image.QImage> images = this.<dblab.sharing_flatform.domain.image.Image, dblab.sharing_flatform.domain.image.QImage>createList("images", dblab.sharing_flatform.domain.image.Image.class, dblab.sharing_flatform.domain.image.QImage.class, PathInits.DIRECT2);

    public final dblab.sharing_flatform.domain.item.QItem item;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModified = _super.lastModified;

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final dblab.sharing_flatform.domain.member.QMember member;

    public final StringPath title = createString("title");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new dblab.sharing_flatform.domain.category.QCategory(forProperty("category"), inits.get("category")) : null;
        this.item = inits.isInitialized("item") ? new dblab.sharing_flatform.domain.item.QItem(forProperty("item")) : null;
        this.member = inits.isInitialized("member") ? new dblab.sharing_flatform.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

