package com.jumia.validator.service.impl;

import com.jumia.validator.enums.CountryEnum;
import com.jumia.validator.enums.StateEnum;
import com.jumia.validator.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

class PhoneServiceImplTest {

    private static final CountryEnum COUNTRY = CountryEnum.valueOf("CAMEROON");
    private static final String INVALID_COUNTRY = "cou";
    private static final String PHONE_NUMBER = "(212) 698054317";

    private PhoneServiceImpl phoneService;
    private PhoneServiceImpl phoneServiceSpy;
    private CountryService countryService;

    @BeforeEach
    void init() {
        countryService = Mockito.mock(CountryService.class);
        phoneService = new PhoneServiceImpl(countryService);
        phoneServiceSpy = Mockito.spy(phoneService);
    }

    @Test
    void getPhoneNumberState_ValidPhoneNumberWithValidCountry_ValidState() {

        doReturn(COUNTRY).when(countryService).findByName(COUNTRY.getName());
        doReturn(StateEnum.VALID).when(phoneServiceSpy).validatePhoneNumber(PHONE_NUMBER, COUNTRY);

        StateEnum result = phoneServiceSpy.getPhoneNumberState(PHONE_NUMBER, COUNTRY.getName());

        assertNotNull(result);
        assertEquals(StateEnum.VALID, result);
    }

    @Test
    void getPhoneNumberState_ValidPhoneNumberWithInValidCountry_InvalidState() {

        doReturn(null).when(countryService).findByName(INVALID_COUNTRY);
        StateEnum result = phoneServiceSpy.getPhoneNumberState(PHONE_NUMBER, INVALID_COUNTRY);

        assertNotNull(result);
        assertEquals(StateEnum.INVALID, result);
    }

    @Test
    void getPhoneNumberState_NullPhoneNumberWithNullCountry_InvalidState() {

        String country = null;
        String phoneNumber = null;

        doReturn(null).when(countryService).findByName(country);
        StateEnum result = phoneServiceSpy.getPhoneNumberState(phoneNumber, country);

        assertNotNull(result);
        assertEquals(StateEnum.INVALID, result);
    }

    @Test
    void getPhoneNumberState_NullPhoneNumberWithValidCountry_InValidState() {

        doReturn(COUNTRY).when(countryService).findByName(COUNTRY.getName());
        doReturn(StateEnum.INVALID).when(phoneServiceSpy).validatePhoneNumber(PHONE_NUMBER, COUNTRY);
        StateEnum result = phoneServiceSpy.getPhoneNumberState(PHONE_NUMBER, COUNTRY.getName());

        assertNotNull(result);
        assertEquals(StateEnum.INVALID, result);
    }
}
