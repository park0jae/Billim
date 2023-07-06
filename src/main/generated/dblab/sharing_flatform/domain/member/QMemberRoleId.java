package dblab.sharing_flatform.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberRoleId is a Querydsl query type for MemberRoleId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMemberRoleId extends BeanPath<MemberRoleId> {

    private static final long serialVersionUID = -1316550140L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberRoleId memberRoleId = new QMemberRoleId("memberRoleId");

    public final QMember member;

    public final dblab.sharing_flatform.domain.role.QRole role;

    public QMemberRoleId(String variable) {
        this(MemberRoleId.class, forVariable(variable), INITS);
    }

    public QMemberRoleId(Path<? extends MemberRoleId> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberRoleId(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberRoleId(PathMetadata metadata, PathInits inits) {
        this(MemberRoleId.class, metadata, inits);
    }

    public QMemberRoleId(Class<? extends MemberRoleId> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.role = inits.isInitialized("role") ? new dblab.sharing_flatform.domain.role.QRole(forProperty("role")) : null;
    }

}

