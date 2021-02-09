package com.softserve.itacademy.service.mailing;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class MailMessage implements Serializable {

    private String email;
    private String message;
    private String subject;

    @JsonCreator
    public MailMessage(String email, String message, String subject) {
        this.email = email;
        this.message = message;
        this.subject = subject;
    }
}
