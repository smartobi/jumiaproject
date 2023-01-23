package com.jumia.validator.service.impl;

import com.jumia.validator.enums.CountryEnum;
import com.jumia.validator.enums.StateEnum;
import com.jumia.validator.service.CountryService;
import com.jumia.validator.service.PhoneService;
import com.jumia.validator.service.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service

public class PhoneServiceImpl implements PhoneService {

    private CountryService countryService;

    /**
     * Get the phone number validation state.
     *
     * @param phoneNumber the phone number
     * @param country     the country of the phone number
     * @return a validation enum
     */
    @Override
    public StateEnum getPhoneNumberState(String phoneNumber, String country) {
        CountryEnum countryEnum = countryService.findByName(country);
        if (countryEnum == null) {
            return StateEnum.INVALID;
        }
        return validatePhoneNumber(phoneNumber, countryEnum);
    }

    StateEnum validatePhoneNumber(String phoneNumber, CountryEnum countryEnum) {
        String phoneNumberRegex = countryEnum.getPhoneRegex();
        return ValidationUtil.validateNumber(phoneNumber, phoneNumberRegex);
    }

}
