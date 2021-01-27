package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ChatMessage extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "chat_room_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_message_chat_room"))
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_user_message"))
    private User user;

    @Length(min = 1, max = 4096)
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    public enum MessageStatus {
        RECEIVED, DELIVERED
    }
}
