package com.jumia.validator.service;

import com.jumia.validator.enums.CountryEnum;

public interface CountryService {

    /**
     * Get Country by its name
     *
     * @param name the country name
     * @return the country enum, null if not found
     */
    CountryEnum findByName(String name);

    /**
     * Get a country from phone number.
     *
     * @param phoneNumber the phone number
     * @return a country enum, null if not found
     */
    CountryEnum findByPhoneNumber(String phoneNumber);
}
