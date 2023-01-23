package com.jumia.validator.domain.dto;

import com.jumia.validator.enums.StateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class CustomerFilterDTO {

    private String country;
    private StateEnum state;
}
