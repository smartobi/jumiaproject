package com.jumia.validator.service.util;

import com.jumia.validator.enums.StateEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    public static final String COUNTRY_CODE = "212";
    private static final String VALID_NUM = "(212) 698054317";
    private static final String INVALID_NUM = "(212 698054317";
    private static final String VALID_INPUT = "(212) 698054317";
    private static final String INVALID_INPUT = "(2123) 698054317";
    private static final String VALID_REGEX = "\\(212\\)\\ ?[5-9]\\d{8}$";
    private static final String INVALID_REGEX = "\\(212\\)\\ ?[5-9]\\d{8}[][[$";
    private static final String EMPTY = "";
    private static final String NULL = null;

    @Test
    void getCountryCode_ValidNumber_CorrectCode() {

        String result = ValidationUtil.getCountryCode(VALID_NUM);
        assertEquals(COUNTRY_CODE, result);

    }

    @Test
    void getCountryCode_InValidNumber_ReturnNull() {

        String result = ValidationUtil.getCountryCode(INVALID_NUM);
        assertNull(result);

    }

    @Test
    void getCountryCode_NullNumber_returnNull() {

        String result = ValidationUtil.getCountryCode(NULL);
        assertNull(result);

    }


    @Test
    void validateNumber_ValidRegexWithValidInput_ValidState() {

        StateEnum result = ValidationUtil.validateNumber(VALID_INPUT, VALID_REGEX);
        assertEquals(StateEnum.VALID, result);

    }

    @Test
    void validateNumber_NullRegexWithValidInput_InValidState() {

        StateEnum result = ValidationUtil.validateNumber(VALID_INPUT, NULL);
        assertEquals(StateEnum.INVALID, result);

    }

    @Test
    void validateNumber_ValidRegexWithNullInput_InValidState() {

        StateEnum result = ValidationUtil.validateNumber(NULL, VALID_REGEX);
        assertEquals(StateEnum.INVALID, result);

    }

    @Test
    void validateNumber_assertNullRegexWithNullInput_InValidState() {

        StateEnum result = ValidationUtil.validateNumber(NULL, NULL);
        assertEquals(StateEnum.INVALID, result);

    }

    @Test
    void validateNumber_ValidRegexWithInvalidInput_InValidState() {

        StateEnum result = ValidationUtil.validateNumber(INVALID_INPUT, VALID_REGEX);
        assertEquals(StateEnum.INVALID, result);

    }

    @Test
    void validateNumber_InValidRegexWithValidInput_InValidState() {

        StateEnum result = ValidationUtil.validateNumber(VALID_INPUT, INVALID_REGEX);
        assertEquals(StateEnum.INVALID, result);

    }

    @Test
    void validateNumber_EmptyRegexWithValidInput_InValidState() {

        StateEnum result = ValidationUtil.validateNumber(EMPTY, INVALID_INPUT);
        assertEquals(StateEnum.INVALID, result);

    }

    @Test
    void isParametersValid_EmptyRegexWithEmptyInput_False() {

        boolean result = ValidationUtil.isParametersValid(EMPTY, EMPTY);
        assertFalse(result);

    }

    @Test
    void isParametersValid_ValidRegexWithValidInput_True() {

        boolean result = ValidationUtil.isParametersValid(INVALID_INPUT, VALID_REGEX);
        assertTrue(result);

    }

    @Test
    void isParametersValid_EmptyRegexWithNullInput_False() {

        boolean result = ValidationUtil.isParametersValid(NULL, EMPTY);
        assertFalse(result);

    }

}
