package com.jumia.validator.domain.dto;

import com.jumia.validator.enums.StateEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class CustomerDTO {

    private Long id;
    private String name;
    private String phone;
    private String country;
    private StateEnum phoneNumberState;
}
