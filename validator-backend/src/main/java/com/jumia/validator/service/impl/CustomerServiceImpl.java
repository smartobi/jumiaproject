package com.jumia.validator.service.impl;

import com.jumia.validator.domain.dto.CustomerDTO;
import com.jumia.validator.domain.dto.CustomerFilterDTO;
import com.jumia.validator.domain.entity.Customer;
import com.jumia.validator.enums.CountryEnum;
import com.jumia.validator.enums.StateEnum;
import com.jumia.validator.mapper.CustomerMapper;
import com.jumia.validator.repository.CustomerRepository;
import com.jumia.validator.service.CountryService;
import com.jumia.validator.service.CustomerService;
import com.jumia.validator.service.PhoneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CountryService countryService;
    private PhoneService phoneService;
    private CustomerMapper customerMapper;


    /**
     * Get all the customers.
     *
     * @param customerFilterDTO the filters information
     * @return the list of customers
     */
    @Override
    public List<CustomerDTO> findAll(CustomerFilterDTO customerFilterDTO) {
        List<CustomerDTO> customers = getCustomersDTO(customerRepository.findAll());
        fillCustomerFields(customers);
        customers = filterCustomersList(customers, customerFilterDTO);

        return customers;
    }

    void fillCustomerFields(List<CustomerDTO> customers) {
        fillCustomersCountries(customers);
        fillCustomersPhoneNumbersState(customers);
    }

    List<CustomerDTO> getCustomersDTO(List<Customer> customers) {

        return customerMapper.CustomersToCustomersDto(customers);
    }

    void fillCustomersCountries(List<CustomerDTO> customers) {
        customers.stream()
                .map(this::getCustomerCountry).collect(Collectors.toList());
    }

    void fillCustomersPhoneNumbersState(List<CustomerDTO> customers) {
        customers.stream()
                .map(this::getCustomerPhoneNumberState).collect(Collectors.toList());
    }

    CustomerDTO getCustomerCountry(CustomerDTO customer) {
        CountryEnum country = countryService.findByPhoneNumber(customer.getPhone());
        if (country == null) {
            return customer;
        }
        customer.setCountry(country.getName());
        return customer;
    }

    CustomerDTO getCustomerPhoneNumberState(CustomerDTO customer) {
        StateEnum stateEnum = phoneService.getPhoneNumberState(customer.getPhone(), customer.getCountry());
        customer.setPhoneNumberState(stateEnum);
        return customer;
    }

    List<CustomerDTO> filterCustomersList(List<CustomerDTO> customers, CustomerFilterDTO customerFilterDTO) {
        return customers.stream()
                .filter(getCountryFilterPredicate(customerFilterDTO))
                .filter(getStateFilterPredicate(customerFilterDTO))
                .collect(Collectors.toList());
    }

    Predicate<CustomerDTO> getStateFilterPredicate(CustomerFilterDTO customerFilterDTO) {
        return customer -> customerFilterDTO.getState() == null || customer.getPhoneNumberState().equals(customerFilterDTO.getState());
    }

    Predicate<CustomerDTO> getCountryFilterPredicate(CustomerFilterDTO customerFilterDTO) {
        return customer -> StringUtils.isEmpty(customerFilterDTO.getCountry()) || StringUtils.equalsIgnoreCase(customer.getCountry(), customerFilterDTO.getCountry());
    }
}
