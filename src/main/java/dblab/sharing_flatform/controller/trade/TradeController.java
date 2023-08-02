package dblab.sharing_flatform.controller.trade;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.dto.trade.TradePagingCondition;
import dblab.sharing_flatform.dto.trade.TradeRequestDto;
import dblab.sharing_flatform.service.trade.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static dblab.sharing_flatform.config.security.util.SecurityUtil.getCurrentUsernameCheck;

@Api(value = "Trade Controller", tags = "Trade")
@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {
    private final TradeService tradeService;

    @ApiOperation(value = "Post ID로 Trade 생성", notes = "게시글 ID로 Trade를 생성합니다.")
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createTrade(@Valid @RequestBody TradeRequestDto tradeRequestDto,
                                @ApiParam(value = "Post ID", required = true) @PathVariable Long postId) {
        return Response.success(tradeService.createTrade(tradeRequestDto, postId, getCurrentUsernameCheck()));
    }

    @ApiOperation(value = "Trade ID로 Trade의 거래완료", notes = "Trade ID로 해당 Trade의 거래를 완료합니다.")
    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response completeTrade(@ApiParam(value = "완료할 Trade ID", required = true) @PathVariable Long postId){
        return Response.success(tradeService.completeTrade(postId));
    }

    @ApiOperation(value = "Trade ID로 거래를 단건 조회", notes = "Trade ID로 거래를 조회합니다.")
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response findTradeById(@ApiParam(value = "조회할 Trade ID", required = true) @PathVariable Long postId){
        return Response.success(tradeService.findTradeById(postId));
    }

    @ApiOperation(value = "Trade 전체 조회", notes = "모든 Trade를 조회합니다 / ADMIN 전용")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response findAllTrade(@Valid TradePagingCondition cond){
        return Response.success(tradeService.findAllByCond(cond));
    }

    @ApiOperation(value = "나의 완료된/완료되지 않은 수여 거래 내역 조회", notes = "내가 대여 해줄 거래 중 완료된/아직 완료되지 않은 거래 내역 조회")
    @GetMapping("/rend")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllMyRendTrade(@Valid TradePagingCondition cond){
        cond.setRenderMember(SecurityUtil.getCurrentUsernameCheck());
        return Response.success(tradeService.findAllByMyRendTrade(cond));
    }


    @ApiOperation(value = "나의 완료된/완료되지 않은 대여 거래 내역 조회", notes = "내가 대여 받을 거래 중 완료된/아직 완료되지 않은 거래 내역 조회")
    @GetMapping("/borrow")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllMyBorrowTrade(@Valid TradePagingCondition cond){
        cond.setBorrowerMember(SecurityUtil.getCurrentUsernameCheck());
        return Response.success(tradeService.findAllByMyBorrowTrade(cond));
    }

    @ApiOperation(value = "Trade ID로 거래를 삭제", notes = "Trade ID로 거래를 삭제합니다.")
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response cancelByRender(@ApiParam(value = "삭제 Trade ID", required = true) @PathVariable Long postId){
        tradeService.deleteTrade(postId);
        return Response.success();
    }

}