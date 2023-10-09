package dblab.sharing_platform.controller.trade;

import dblab.sharing_platform.config.security.util.SecurityUtil;
import dblab.sharing_platform.dto.trade.TradePagingCondition;
import dblab.sharing_platform.dto.trade.TradeRequest;
import dblab.sharing_platform.service.trade.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static dblab.sharing_platform.config.security.util.SecurityUtil.getCurrentUsernameCheck;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Trade Controller", tags = "Trade")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;

    @ApiOperation(value = "Post ID로 Trade 생성", notes = "게시글 ID로 Trade를 생성합니다.")
    @PostMapping("/{postId}")
    public ResponseEntity createTradeByPostId(@Valid @RequestBody TradeRequest tradeRequest,
                                              @ApiParam(value = "Post ID", required = true) @PathVariable Long postId) {
        return new ResponseEntity(tradeService.createTradeByPostId(tradeRequest, postId, getCurrentUsernameCheck()), CREATED);
    }

    @ApiOperation(value = "Trade ID로 Trade의 거래완료", notes = "Trade ID로 해당 Trade의 거래를 완료합니다.")
    @PatchMapping("/trade/{tradeId}")
    public ResponseEntity completeTradeByTradeId(@ApiParam(value = "완료할 Trade ID", required = true) @PathVariable Long tradeId){
        return new ResponseEntity(tradeService.completeTradeByTradeId(tradeId), OK);
    }

    @ApiOperation(value = "Trade ID로 거래를 단건 조회", notes = "Trade ID로 거래를 조회합니다.")
    @GetMapping("/{tradeId}")
    public ResponseEntity findSingleTradeById(@ApiParam(value = "조회할 Trade ID", required = true) @PathVariable Long tradeId){
        return new ResponseEntity(tradeService.findSingleTradeById(tradeId), OK);
    }

    @ApiOperation(value = "Trade 전체 조회", notes = "모든 Trade를 조회합니다 / ADMIN 전용")
    @GetMapping
    public ResponseEntity findAllTradeByAdmin(@Valid TradePagingCondition condition){
        return new ResponseEntity(tradeService.findAllTradeByAdmin(condition), OK);
    }

    @ApiOperation(value = "나의 완료된/완료되지 않은 수여 거래 내역 조회", notes = "내가 대여 해줄 거래 중 완료된/아직 완료되지 않은 거래 내역 조회")
    @GetMapping("/rend-item")
    public ResponseEntity findAllRendTradeByCurrentUser(@Valid TradePagingCondition condition){
        condition.setRenderMember(SecurityUtil.getCurrentUsernameCheck());
        return new ResponseEntity(tradeService.findAllRendTradeByCurrentUser(condition), OK);
    }

    @ApiOperation(value = "나의 완료된/완료되지 않은 대여 거래 내역 조회", notes = "내가 대여 받을 거래 중 완료된/아직 완료되지 않은 거래 내역 조회")
    @GetMapping("/borrow-item")
    public ResponseEntity findAllBorrowTradeByCurrentUser(@Valid TradePagingCondition condition){
        condition.setBorrowerMember(SecurityUtil.getCurrentUsernameCheck());
        return new ResponseEntity(tradeService.findAllBorrowTradeByCurrentUser(condition), OK);
    }

    @ApiOperation(value = "Trade ID로 거래를 삭제", notes = "Trade ID로 거래를 삭제합니다.")
    @DeleteMapping("/{tradeId}")
    public ResponseEntity cancelTradeByRenderWithTradeId(@ApiParam(value = "삭제 Trade ID", required = true) @PathVariable Long tradeId){
        tradeService.deleteTradeByRenderWithTradeId(tradeId);
        return new ResponseEntity(OK);
    }
}