package com.jumia.validator.service;

import com.jumia.validator.enums.StateEnum;

public interface PhoneService {

    /**
     * Get the phone number validation state.
     *
     * @param phoneNumber the phone number
     * @param country  the country of the phone number
     * @return a validation enum
     */
    StateEnum getPhoneNumberState(String phoneNumber, String country);

}
