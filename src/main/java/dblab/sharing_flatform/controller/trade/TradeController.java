package dblab.sharing_flatform.controller.trade;

import dblab.sharing_flatform.aop.AssignUsername;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.dto.trade.crud.create.TradeRequestDto;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.trade.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        tradeRequestDto.setUsername(SecurityUtil.getCurrentUsernameCheck());
        return Response.success(tradeService.createTrade(tradeRequestDto, postId));
    }

    @ApiOperation(value = "Trade ID로 Trade의 거래완료", notes = "거래 ID로 Trade의 거래를 완료합니다.")
    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response completeTrade(@ApiParam(value = "완료할 Trade ID", required = true) @PathVariable Long postId){
        return Response.success(tradeService.completeTrade(postId));
    }

    @ApiOperation(value = "Trade ID로 거래를 조회", notes = "Trade ID로 거래를 조회합니다.")
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response findTradeById(@ApiParam(value = "조회할 Trade ID", required = true) @PathVariable Long postId){
        return Response.success(tradeService.findTradeById(postId));
    }

    @ApiOperation(value = "Trade 전체 조회", notes = "모든 Trade를 조회합니다 / ADMIN 전용")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response findAllTrade(){
        return Response.success(tradeService.findAllTrade());
    }

    @ApiOperation(value = "Trade ID로 거래를 삭제", notes = "Trade ID로 거래를 삭제합니다.")
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public Response cancelByRender(@ApiParam(value = "삭제 Trade ID", required = true) @PathVariable Long postId){
        tradeService.deleteTrade(postId);
        return Response.success();
    }


}