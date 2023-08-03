package dblab.sharing_flatform.domain.chat;

import dblab.sharing_flatform.domain.base.BaseTime;
import dblab.sharing_flatform.dto.chat.chatMessage.ChatMessageRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_table")
@Entity
public class ChatMessage extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private String writer;

    @Column
    private String message;

    public ChatMessage(ChatRoom chatRoom, String writer,  String message){
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.message = message;
    }


    public static ChatMessage toEntity(ChatMessageRequestDto saveDto, ChatRoom chatRoom){
        return new ChatMessage(chatRoom, saveDto.getWriter(), saveDto.getMessage());

    }
}
