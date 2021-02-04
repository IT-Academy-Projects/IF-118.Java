package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_events_users_recipient_id"))
    private User recipient;

    private Integer subjectId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventType type;



    public enum EventType {
        CREATE_ASSIGNMENT,
        GRADE_ANSWER,
        SUBMIT_ANSWER,
        INVITE
    }

}
