package dblab.sharing_flatform.config.security.guard.review;

import dblab.sharing_flatform.config.security.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class ReviewOwnerGuard extends Guard {

    @Override
    protected boolean isResourceOwner(Long id) {
        return false;
    }
}
