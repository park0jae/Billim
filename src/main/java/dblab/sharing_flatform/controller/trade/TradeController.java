package dblab.sharing_flatform.controller.trade;

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
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createTrade(@Valid @RequestBody TradeRequestDto tradeRequestDto,
                                @ApiParam(value = "Post ID", required = true) @PathVariable Long id) {
        tradeRequestDto.setRenderName(SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new));
        return Response.success(tradeService.createTrade(tradeRequestDto, id));
    }

    @ApiOperation(value = "Trade ID로 Trade의 거래완료", notes = "거래 ID로 Trade의 거래를 완료합니다.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response completeTrade(@ApiParam(value = "완료할 Trade ID", required = true) @PathVariable Long id){
        return Response.success(tradeService.completeTrade(id));
    }

    @ApiOperation(value = "Trade ID로 거래를 조회", notes = "Trade ID로 거래를 조회합니다.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findTradeById(@ApiParam(value = "조회할 Trade ID", required = true) @PathVariable Long id){
        return Response.success(tradeService.findTradeById(id));
    }

    @ApiOperation(value = "Trade 전체 조회", notes = "모든 Trade를 조회합니다 / ADMIN 전용")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response findAllTrade(){
        return Response.success(tradeService.findAllTrade());
    }

    @ApiOperation(value = "Trade ID로 거래를 삭제", notes = "Trade ID로 거래를 삭제합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response cancelByRender(@ApiParam(value = "삭제 Trade ID", required = true) @PathVariable Long id){
        tradeService.deleteTrade(id);
        return Response.success();
    }
}