package com.jumia.validator.service.impl;

import com.jumia.validator.enums.CountryEnum;
import com.jumia.validator.service.CountryService;
import com.jumia.validator.service.util.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CountryServiceImpl implements CountryService {

    /**
     * Get Country by its name
     *
     * @param name the country name
     * @return the country enum, null if not found
     */
    @Override
    public CountryEnum findByName(String name) {
        return Stream.of(CountryEnum.values())
                .filter(country -> StringUtils.equalsIgnoreCase(country.getName(), name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a country from phone number.
     *
     * @param phoneNumber the phone number
     * @return a country enum, null if not found
     */
    @Override
    public CountryEnum findByPhoneNumber(String phoneNumber) {
        String countryCode = getCountryCode(phoneNumber);
        return getCountryByCode(countryCode);
    }

    /**
     *  Get a country from country code.
     *
     * @param countryCode
     * @return a country enum, null if not found
     */
    CountryEnum getCountryByCode(String countryCode) {
        return Stream.of(CountryEnum.values())
                .filter(country -> StringUtils.equals(country.getPhoneCode(), countryCode))
                .findFirst()
                .orElse(null);
    }

    String getCountryCode(String number) {
        return ValidationUtil.getCountryCode(number);
    }
}
