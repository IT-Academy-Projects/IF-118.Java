package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialExpirationRequest {
    private LocalDateTime startDate;
    private LocalDateTime expirationDate;
    private Integer groupId;
    private Integer materialId;
}
