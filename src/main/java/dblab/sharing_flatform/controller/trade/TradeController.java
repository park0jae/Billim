package dblab.sharing_flatform.controller.trade;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.dto.trade.crud.create.TradeRequestDto;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    public Response createTrade(@Valid @RequestBody TradeRequestDto tradeRequestDto) {
        return Response.success(tradeService.createTrade(tradeRequestDto));
    }

    @GetMapping("/{id}")
    public Response findTradeById(@PathVariable Long id){
        return Response.success(tradeService.findTradeById(id));
    }

    @GetMapping
    public Response findAllTrade(){
        return Response.success(tradeService.findAllTrade());
    }

    // 취소하고자 하는 trade의 ID를 넘기면 , 그 거래의 render id와 현재 취소하려는 id가 일치하면 지워야 함
    @DeleteMapping("/render/{id}")
    public Response cancelByRender(@PathVariable Long id){
        tradeService.cancelByRender(id);
        return Response.success();
    }

    // 취소하고자 하는 trade의 id를 넘기면, 그 거래의 borrower id와 현재 취소하려는 id가 일치하면 지워야 함
    @DeleteMapping("/borrower/{id}")
    public Response cancelByBorrower(@PathVariable Long id){
        tradeService.cancelByBorrower(id);
        return Response.success();
    }
}