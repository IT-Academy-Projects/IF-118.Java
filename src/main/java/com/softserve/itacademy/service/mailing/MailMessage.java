package com.softserve.itacademy.service.mailing;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class MailMessage implements Serializable {

    private String email;
    private String message;
    private String subject;

    private LocalDateTime expirationDate;

    @JsonCreator
    public MailMessage(String email, String message, String subject, LocalDateTime expirationDate) {
        this.email = email;
        this.message = message;
        this.subject = subject;
        this.expirationDate = expirationDate;
    }
}
