package dblab.sharing_flatform.domain.chat;

import dblab.sharing_flatform.domain.base.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatRoom_table")
@Entity
public class ChatRoom extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    private String roomId;
    private String roomName;
    private String chatMentor;

    public ChatRoom(String roomName, String roomId, String chatMentor){
        this.roomName = roomName;
        this.roomId = roomId;
        this.chatMentor = chatMentor;
    }
}

