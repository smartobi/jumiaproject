package com.jumia.validator.service.util;

import com.jumia.validator.enums.StateEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
@UtilityClass
public class ValidationUtil {

    /**
     * get the country code between "(" and ")"
     * @param number
     * @return country code
     */

    public String getCountryCode(String number) {
        return StringUtils.substringBetween(number, "(", ")");
    }

    /**
     * Validate input string with a regex
     *
     * @param Number the input string
     * @param regex  the regex to match
     * @return the state enum
     */
    public StateEnum validateNumber(String Number, String regex) {
        try {
            if (isParametersValid(Number, regex) && Pattern.matches(regex, Number)) {
                return StateEnum.VALID;
            }
        } catch (PatternSyntaxException e) {
            log.error("Error in validating regex {}", e);
        }
        return StateEnum.INVALID;
    }

    boolean isParametersValid(String input, String regex) {
        return StringUtils.isNoneEmpty(input) && StringUtils.isNoneEmpty(regex);
    }
}
