package dblab.sharing_flatform.advisor;

import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.ValidateTokenException;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.auth.AuthenticationEntryPointException;
import dblab.sharing_flatform.exception.auth.IllegalAuthenticationException;
import dblab.sharing_flatform.exception.auth.LoginFailureException;
import dblab.sharing_flatform.exception.category.CategoryNotFoundException;
import dblab.sharing_flatform.exception.comment.CommentNotFoundException;
import dblab.sharing_flatform.exception.comment.RootCommentNotFoundException;
import dblab.sharing_flatform.exception.guard.GuardException;
import dblab.sharing_flatform.exception.image.NoExtException;
import dblab.sharing_flatform.exception.image.UnSupportExtException;
import dblab.sharing_flatform.exception.member.DuplicateUsernameException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.message.MessageNotFoundException;
import dblab.sharing_flatform.exception.oauth.OAuthCommunicationException;
import dblab.sharing_flatform.exception.oauth.OAuthUserNotFoundException;
import dblab.sharing_flatform.exception.oauth.SocialAgreementException;
import dblab.sharing_flatform.exception.post.PostNotFoundException;
import dblab.sharing_flatform.exception.review.ExistReviewException;
import dblab.sharing_flatform.exception.review.ImpossibleWriteReviewException;
import dblab.sharing_flatform.exception.review.ReviewNotFoundException;
import dblab.sharing_flatform.exception.role.RoleNotFoundException;
import dblab.sharing_flatform.exception.trade.ExistTradeException;
import dblab.sharing_flatform.exception.trade.ImpossibleCreateTradeException;
import dblab.sharing_flatform.exception.trade.TradeNotCompleteException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvisor {

    // uncaught Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        e.printStackTrace();
        return Response.failure(500, e.getMessage());
    }

    // Field Error
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        log.info("message = {}", e.getMessage());
        return Response.failure(400, "양식에 맞게 입력해주세요.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, "양식에 맞게 입력해주세요.");
    }

    // auth
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response accessDeniedException(AccessDeniedException e) {
        return Response.failure(400, "해당 권한으로 수행할 수 없는 작업입니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response authenticationEntryPointException(AuthenticationEntryPointException e) {
        return Response.failure(400, "로그인이 필요한 요청입니다.");
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response loginFailureException(LoginFailureException e) {
        return Response.failure(400, "로그인에 실패하였습니다. 아이디나 비밀번호를 확인해주세요.");
    }

    @ExceptionHandler(IllegalAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response illegalAuthenticationException(IllegalAuthenticationException e) {
        return Response.failure(400, "올바르지 않은 인증정보입니다.");
    }

    @ExceptionHandler(ValidateTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response validateTokenException(ValidateTokenException e) {
        return Response.failure(400, "검증되지 않은 토큰 정보입니다.");
    }

    // image
    @ExceptionHandler(NoExtException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response noExtException(NoExtException e) {
        return Response.failure(404, "사진의 확장자를 찾을 수 없습니다.");
    }

    @ExceptionHandler(UnSupportExtException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Response unSupportExtException(UnSupportExtException e) {
        return Response.failure(415, "지원하지 않는 미디어 타입입니다.");
    }


    // member
    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response duplicateUsernameException(DuplicateUsernameException e) {
        return Response.failure(409, "이미 사용중인 Username입니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException(MemberNotFoundException e) {
        return Response.failure(404, "회원을 찾을 수 없습니다.");
    }


    // role
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException(RoleNotFoundException e) {
        return Response.failure(404, "권한을 찾을 수 없습니다.");
    }


    // category
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException(CategoryNotFoundException e) {
        return Response.failure(404, "지정한 카테고리를 찾을 수 없습니다.");
    }

    // message
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response messageNotFoundException(MessageNotFoundException e) {
        return Response.failure(404, "메시지를 찾을 수 없습니다.");
    }


    // post
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response postNotFoundException(PostNotFoundException e) {
        return Response.failure(404, "게시글을 찾을 수 없습니다.");
    }


    // oauth
    @ExceptionHandler(OAuthUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response oAuthUserNotFoundException(OAuthUserNotFoundException e) {
        return Response.failure(404, "OAuth 소셜 유저를 찾을 수 없습니다.");
    }

    @ExceptionHandler(SocialAgreementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response socialAgreementException(SocialAgreementException e) {
        return Response.failure(400, "OAuth 정보 제공 동의 관련 오류가 발생했습니다.");
    }

    @ExceptionHandler(OAuthCommunicationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response oAuthCommunicationException(OAuthCommunicationException e) {
        return Response.failure(500, "OAuth 연결 해제 링크과의 연결 중 문제가 발생했습니다.");
    }

    // comment
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException(CommentNotFoundException e) {
        return Response.failure(404, "존재하지 않는 댓글입니다.");
    }

    @ExceptionHandler(RootCommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response rootCommentNotFoundException(RootCommentNotFoundException e) {
        return Response.failure(404, "상위 댓글이 존재하지 않습니다.");
    }

    // trade
    @ExceptionHandler(TradeNotCompleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response tradeNotCompleteException(TradeNotCompleteException e) {
        return Response.failure(400, "완료되지 않은 거래에 대해서는 리뷰 작성이 불가능합니다. 거래를 완료해주세요.");
    }

    @ExceptionHandler(ImpossibleCreateTradeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response impossibleCreateTradeException(ImpossibleCreateTradeException e) {
        return Response.failure(400, "거래를 시작할 수 없습니다.");
    }

    @ExceptionHandler(TradeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response tradeNotFoundException(TradeNotFoundException e) {
        return Response.failure(404, "거래 내역이 존재하지 않습니다.");
    }

    @ExceptionHandler(ExistTradeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response ExistTradeException(ExistTradeException e) {
        return Response.failure(409, "해당 거래가 이미 진행중입니다.");
    }

    // review
    @ExceptionHandler(ImpossibleWriteReviewException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response impossibleWriteReviewException(ImpossibleWriteReviewException e) {
        return Response.failure(400, "해당 거래에 대해 리뷰를 작성할 권한이 없습니다.");
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response reviewNotFoundException(ReviewNotFoundException e) {
        return Response.failure(404, "리뷰가 존재하지 않습니다.");
    }

    @ExceptionHandler(ExistReviewException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response ExistReviewException(ExistReviewException e) {
        return Response.failure(409, "해당 거래에 대한 리뷰가 이미 존재합니다.");
    }

    // Guard Exception
    @ExceptionHandler(GuardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response guardException(GuardException e) {
        return Response.failure(400, "비정상적인 접근입니다.");
    }

}
