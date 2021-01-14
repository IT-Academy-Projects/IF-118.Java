package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class GroupChatRoom extends BasicEntity {

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "group_id", referencedColumnName = "id")
//    private Group group;

    @OneToOne(mappedBy = "groupChatRoom", cascade = CascadeType.ALL)
    private Group group;

    @OneToMany(mappedBy = "groupChatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages;
}
