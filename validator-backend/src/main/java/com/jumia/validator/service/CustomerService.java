package com.jumia.validator.service;

import com.jumia.validator.domain.dto.CustomerDTO;
import com.jumia.validator.domain.dto.CustomerFilterDTO;

import java.util.List;

public interface CustomerService {

    /**
     * Get all the customers.
     *
     * @param customerFilterDTO the filters information
     * @return the list of customers
     */
    List<CustomerDTO> findAll(CustomerFilterDTO customerFilterDTO);
}
