package dblab.sharing_flatform.domain.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessage is a Querydsl query type for Message
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessage extends EntityPathBase<Message> {

    private static final long serialVersionUID = 177600293L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessage message = new QMessage("message");

    public final StringPath content = createString("content");

    public final BooleanPath deleteByReceiver = createBoolean("deleteByReceiver");

    public final BooleanPath deleteBySender = createBoolean("deleteBySender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final dblab.sharing_flatform.domain.member.QMember receiveMember;

    public final dblab.sharing_flatform.domain.member.QMember sendMember;

    public QMessage(String variable) {
        this(Message.class, forVariable(variable), INITS);
    }

    public QMessage(Path<? extends Message> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessage(PathMetadata metadata, PathInits inits) {
        this(Message.class, metadata, inits);
    }

    public QMessage(Class<? extends Message> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiveMember = inits.isInitialized("receiveMember") ? new dblab.sharing_flatform.domain.member.QMember(forProperty("receiveMember"), inits.get("receiveMember")) : null;
        this.sendMember = inits.isInitialized("sendMember") ? new dblab.sharing_flatform.domain.member.QMember(forProperty("sendMember"), inits.get("sendMember")) : null;
    }

}

