package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "events")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_events_users_creator_id"))
    private User creator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "events_recipients",
            joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__events__users__event_id"))},
            inverseJoinColumns = {@JoinColumn(name = "recipient_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_events_users_recipient_id"))}
    )
    private List<User> recipients;

    private Integer entityId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventType type;



    public enum EventType {
        OPEN_LECTION,
        CLOSE_LECTION,
        GRADE_ANSWER,
        REJECT_ANSWER,
        SUBMIT_ANSWER,
        INVITE
    }

}
