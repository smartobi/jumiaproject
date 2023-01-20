package com.jumia.validator.mapper;

import com.jumia.validator.domain.dto.CustomerDTO;
import com.jumia.validator.domain.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    List<CustomerDTO> CustomersToCustomersDto(List<Customer> customer);

}
