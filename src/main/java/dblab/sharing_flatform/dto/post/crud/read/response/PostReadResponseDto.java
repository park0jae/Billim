package dblab.sharing_flatform.dto.post.crud.read.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.ItemDto;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.image.ImageDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReadResponseDto {

    private Long id;
    private String title;
    private String content;
    private MemberDto writer;

    @Nullable
    private ItemDto item;
    private List<ImageDto> imageList;

    @Nullable
    private TradeResponseDto trade;

    public static PostReadResponseDto toDto(Post post) {
        return new PostReadResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                MemberDto.toDto(post.getMember()),
                Optional.ofNullable(ItemDto.toDto(post.getItem())).orElse(null),
                post.getImages().stream().map(i -> ImageDto.toDto(i)).collect(Collectors.toList()),
                Optional.ofNullable(TradeResponseDto.toDto(post.getTrade())).orElse(null)
                );
    }
}
