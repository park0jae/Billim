package dblab.sharing_platform.dto.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {

    private String borrowerName;

    @JsonFormat(pattern = "yyyy.MM.dd") //데이터 포맷 변환
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy.MM.dd") //데이터 포맷 변환
    private LocalDate endDate;
}
