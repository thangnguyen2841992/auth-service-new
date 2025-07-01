package com.regain.auth_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDTO {
    private String formEmail;

    private String toEmail;

    private String activeCode;

    private String toName;

    private int toUserId;

}
