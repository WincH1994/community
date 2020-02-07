package com.soulout.community.dto;

import lombok.Data;

@Data
public class AccesstTokenDTO {

    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
