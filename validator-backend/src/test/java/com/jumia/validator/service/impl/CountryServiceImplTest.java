package com.jumia.validator.service.impl;

import com.jumia.validator.enums.CountryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class CountryServiceImplTest {

    private static final String INVALID_NAME = "";
    private static final CountryEnum COUNTRY = CountryEnum.valueOf("CAMEROON");
    private static final String PHONE_NUMBER = "(212) 698054317";

    private CountryServiceImpl countryService;
    private CountryServiceImpl countryServiceSpy;

    @BeforeEach
    void init() {
        countryService = new CountryServiceImpl();
        countryServiceSpy = Mockito.spy(countryService);
    }


    @Test
    void findByName_ValidName_CorrectEnum() {

        CountryEnum result = countryServiceSpy.findByName(COUNTRY.getName());
        assertNotNull(result);
        assertEquals(COUNTRY, result);
    }

    @Test
    void findByName_InValidName_ReturnNull() {

        CountryEnum result = countryServiceSpy.findByName(INVALID_NAME);
        assertNull(result);
    }

    @Test
    void findByName_NullName_ReturnNull() {

        String name = null;
        CountryEnum result = countryServiceSpy.findByName(name);
        assertNull(result);
    }

    @Test
    void findByPhoneNumber_ValidPhoneNumber_CorrectEnum() {

        doReturn(COUNTRY.getPhoneCode()).when(countryServiceSpy).getCountryCode(PHONE_NUMBER);
        doReturn(COUNTRY).when(countryServiceSpy).getCountryByCode(COUNTRY.getPhoneCode());
        CountryEnum result = countryServiceSpy.findByPhoneNumber(PHONE_NUMBER);
        assertNotNull(result);
        assertEquals(COUNTRY, result);
    }

    @Test
    void findByPhoneNumber_InValidPhoneNumber_ReturnNull() {

        doReturn(COUNTRY.getPhoneCode()).when(countryServiceSpy).getCountryCode(PHONE_NUMBER);
        doReturn(null).when(countryServiceSpy).getCountryByCode(COUNTRY.getPhoneCode());
        CountryEnum result = countryServiceSpy.findByPhoneNumber(PHONE_NUMBER);
        assertNull(result);
    }

    @Test
    void findByPhoneNumber_NullPhoneNumber_ReturnNull() {

        doReturn(COUNTRY.getPhoneCode()).when(countryServiceSpy).getCountryCode(null);
        doReturn(null).when(countryServiceSpy).getCountryByCode(COUNTRY.getPhoneCode());
        CountryEnum result = countryServiceSpy.findByPhoneNumber(null);
        assertNull(result);
    }

    @Test
    void getCountryByCode_NullCode_ReturnNull() {

        CountryEnum result = countryServiceSpy.getCountryByCode(null);
        assertNull(result);
    }

    @Test
    void getCountryByCode_ValidCode_CorrectCountry() {

        CountryEnum result = countryServiceSpy.getCountryByCode(COUNTRY.getPhoneCode());
        assertNotNull(result);
        assertEquals(COUNTRY, result);
    }
}
