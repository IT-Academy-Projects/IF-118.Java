package com.softserve.itacademy.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadFileResponse {

    private byte[] file;
    private String fileName;

}
