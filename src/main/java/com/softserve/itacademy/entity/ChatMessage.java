package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ChatMessage extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "group_chat_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_message_group_chat"))
    private GroupChatRoom groupChatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user_message"))
    private User user;

    private String content;
    private MessageStatus status;

    public enum MessageStatus {
        RECEIVED, DELIVERED
    }
}
