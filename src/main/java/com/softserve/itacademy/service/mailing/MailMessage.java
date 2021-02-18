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
import java.util.Map;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class MailMessage implements Serializable {

    private String email;
    private String subject;
    private String mailTemplate;
    private Map<String, Object> mailContext;

    private LocalDateTime expirationDate;

    @JsonCreator
    public MailMessage(String email, String subject,  String mailTemplate, Map<String, Object> mailContext, LocalDateTime expirationDate) {
        this.email = email;
        this.subject = subject;
        this.mailTemplate = mailTemplate;
        this.mailContext = mailContext;
        this.expirationDate = expirationDate;
    }
}
