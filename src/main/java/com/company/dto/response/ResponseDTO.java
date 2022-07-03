package com.company.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {

    private String massage;
    private Boolean type;

    public ResponseDTO(String massage, Boolean type) {
        this.massage = massage;
        this.type = type;
    }
}
