package com.jumia.validator.controller;

import com.jumia.validator.domain.dto.CustomerDTO;
import com.jumia.validator.domain.dto.CustomerFilterDTO;
import com.jumia.validator.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    /**
     * GET : get all the customers with or without filters
     *
     * @param customerFilterDTO the filtering object
     * @return the ResponseEntity with http status ok (200) and the list of customers in body
     */
    @ApiOperation(value = "get all the filtered customers")
    @GetMapping("")
    public ResponseEntity<List<CustomerDTO>> getCustomers(CustomerFilterDTO customerFilterDTO) {
        log.info("REST request to get a list of customers with filters {}", customerFilterDTO);
        return ResponseEntity.ok(customerService.findAll(customerFilterDTO));
    }
}
