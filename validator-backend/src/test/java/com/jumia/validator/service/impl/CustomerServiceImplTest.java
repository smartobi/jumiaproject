package com.jumia.validator.service.impl;

import com.jumia.validator.domain.dto.CustomerDTO;
import com.jumia.validator.domain.dto.CustomerFilterDTO;
import com.jumia.validator.domain.entity.Customer;
import com.jumia.validator.enums.CountryEnum;
import com.jumia.validator.enums.StateEnum;
import com.jumia.validator.mapper.CustomerMapper;
import com.jumia.validator.repository.CustomerRepository;
import com.jumia.validator.service.CountryService;
import com.jumia.validator.service.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private static final CustomerFilterDTO EMPTY_FILTER = CustomerFilterDTO.builder().build();
    private static final CustomerFilterDTO FILTER_CAMEROON = CustomerFilterDTO.builder().country("CAMEROON").build();
    private static final CustomerFilterDTO FILTER_CAMEROON_INVALID = CustomerFilterDTO.builder().state(StateEnum.INVALID).country("CAMEROON").build();
    private static final CustomerFilterDTO FILTER_VALID = CustomerFilterDTO.builder().state(StateEnum.VALID).build();

    private static final CountryEnum COUNTRY = CountryEnum.valueOf("CAMEROON");

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;
    private CountryService countryService;
    private PhoneService phoneService;
    private CustomerServiceImpl customerService;
    private CustomerServiceImpl customerServiceSpy;

    @BeforeEach
    void init() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        customerMapper = Mockito.mock(CustomerMapper.class);
        countryService = Mockito.mock(CountryService.class);
        phoneService = Mockito.mock(PhoneService.class);
        customerService = new CustomerServiceImpl(customerRepository, countryService, phoneService, customerMapper);
        customerServiceSpy = Mockito.spy(customerService);
    }

    @Test
    void findAll_ValidList() {

        List<CustomerDTO> customers = getCustomersList();
        doReturn(customers).when(customerServiceSpy).getCustomersDTO(anyList());
        doNothing().when(customerServiceSpy).fillCustomerFields(customers);
        doReturn(customers).when(customerServiceSpy).filterCustomersList(customers, FILTER_VALID);
        List<CustomerDTO> result = customerServiceSpy.findAll(FILTER_VALID);
        assertNotNull(result);
        assertEquals(customers.size(), result.size());
        assertEquals(customers, result);

    }

    @Test
    void findAll_ValidListWithNullFilters() {

        List<CustomerDTO> customers = getCustomersList();
        doReturn(customers).when(customerServiceSpy).getCustomersDTO(anyList());
        doNothing().when(customerServiceSpy).fillCustomerFields(customers);
        doReturn(customers).when(customerServiceSpy).filterCustomersList(customers, null);
        List<CustomerDTO> result = customerServiceSpy.findAll(null);
        assertNotNull(result);
        assertEquals(customers.size(), result.size());
        assertEquals(customers, result);

    }

    @Test
    void fillCustomerFields_ValidCalling() {

        List<CustomerDTO> customers = getCustomersList();
        doNothing().when(customerServiceSpy).fillCustomersCountries(customers);
        doNothing().when(customerServiceSpy).fillCustomersPhoneNumbersState(customers);
        customerServiceSpy.fillCustomerFields(customers);
        verify(customerServiceSpy, times(1)).fillCustomersCountries(customers);
        verify(customerServiceSpy, times(1)).fillCustomersPhoneNumbersState(customers);

    }

    @Test
    void getCustomersDTO_ValidCalling() {

        List<CustomerDTO> customersDTO = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        doReturn(customersDTO).when(customerMapper).CustomersToCustomersDto(customers);
        List<CustomerDTO> result = customerServiceSpy.getCustomersDTO(customers);
        verify(customerMapper, times(1)).CustomersToCustomersDto(customers);
    }

    @Test
    void fillCustomersCountries_ValidCountry() {

        List<CustomerDTO> customers = getCustomersList();
        doReturn(COUNTRY).when(countryService).findByPhoneNumber(any());
        customerServiceSpy.fillCustomersCountries(customers);
        verify(customerServiceSpy, times(customers.size())).getCustomerCountry(any());
        customers.forEach(customer -> assertEquals(COUNTRY.getName(), customer.getCountry()));

    }

    @Test
    void fillCustomersCountries_InValidCountry() {

        List<CustomerDTO> customers = getCustomersList();
        doReturn(null).when(countryService).findByPhoneNumber(any());
        customerServiceSpy.fillCustomersCountries(customers);
        verify(customerServiceSpy, times(customers.size())).getCustomerCountry(any());
        customers.forEach(customer -> assertNull( customer.getCountry()));

    }

    @Test
    void fillCustomersPhoneNumbersState_ValidCountry() {

        List<CustomerDTO> customers = getCustomersList();
        doReturn(StateEnum.VALID).when(phoneService).getPhoneNumberState(any(), any());
        customerServiceSpy.fillCustomersPhoneNumbersState(customers);
        verify(customerServiceSpy, times(customers.size())).getCustomerPhoneNumberState(any());
        customers.forEach(customer -> assertEquals(StateEnum.VALID, customer.getPhoneNumberState()));

    }

    @Test
    void fillCustomersPhoneNumbersState_InValidCountry() {

        List<CustomerDTO> customers = getCustomersList();
        doReturn(StateEnum.INVALID).when(phoneService).getPhoneNumberState(any(), any());
        customerServiceSpy.fillCustomersPhoneNumbersState(customers);
        verify(customerServiceSpy, times(customers.size())).getCustomerPhoneNumberState(any());
        customers.forEach(customer -> assertEquals(StateEnum.INVALID, customer.getPhoneNumberState()));

    }

    @Test
    void filterCustomersList_CountryFilter() {

        List<CustomerDTO> customers = getCustomerListToFilter();
        List<CustomerDTO> result = customerServiceSpy.filterCustomersList(customers, FILTER_CAMEROON);
        verify(customerServiceSpy, times(1)).getStateFilterPredicate(FILTER_CAMEROON);
        verify(customerServiceSpy, times(1)).getCountryFilterPredicate(FILTER_CAMEROON);
        assertEquals(2, result.size());

    }

    @Test
    void filterCustomersList_StateFilter() {

        List<CustomerDTO> customers = getCustomerListToFilter();
        List<CustomerDTO> result = customerServiceSpy.filterCustomersList(customers, FILTER_VALID);
        verify(customerServiceSpy, times(1)).getStateFilterPredicate(FILTER_VALID);
        verify(customerServiceSpy, times(1)).getCountryFilterPredicate(FILTER_VALID);
        assertEquals(1, result.size());

    }

    @Test
    void filterCustomersList_StateAndCountryFilter() {

        List<CustomerDTO> customers = getCustomerListToFilter();
        List<CustomerDTO> result = customerServiceSpy.filterCustomersList(customers, FILTER_CAMEROON_INVALID);
        verify(customerServiceSpy, times(1)).getStateFilterPredicate(FILTER_CAMEROON_INVALID);
        verify(customerServiceSpy, times(1)).getCountryFilterPredicate(FILTER_CAMEROON_INVALID);
        assertEquals(1, result.size());

    }

    @Test
    void filterCustomersList_EmptyFilter() {

        List<CustomerDTO> customers = getCustomerListToFilter();
        List<CustomerDTO> result = customerServiceSpy.filterCustomersList(customers, EMPTY_FILTER);
        verify(customerServiceSpy, times(1)).getStateFilterPredicate(EMPTY_FILTER);
        verify(customerServiceSpy, times(1)).getCountryFilterPredicate(EMPTY_FILTER);
        assertEquals(2, result.size());

    }

    private List<CustomerDTO> getCustomerListToFilter() {

        return  new ArrayList<>(Arrays.asList(
                CustomerDTO.builder().id(1L).name("mohamed").phone("(212) 6007989253")
                        .country("Cameroon").phoneNumberState(StateEnum.VALID).build(),
                CustomerDTO.builder().id(2L).name("Ali").phone("(212) 6007989253")
                        .country("Cameroon").phoneNumberState(StateEnum.INVALID).build()));

    }

    private List<CustomerDTO> getCustomersList() {

        return new ArrayList<>(Arrays.asList(
                CustomerDTO.builder().id(1L).name("mohamed").phone("(212) 6007989253").build(),
                CustomerDTO.builder().id(2L).name("Ali").phone("(212) 6007989253").build()));

    }

}
