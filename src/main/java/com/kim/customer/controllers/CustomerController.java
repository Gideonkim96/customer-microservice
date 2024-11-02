package com.kim.customer.controllers;

import com.kim.customer.constants.CustomerConstants;
import com.kim.customer.dto.CustomerDto;
import com.kim.customer.dto.ErrorResponseDto;
import com.kim.customer.dto.ResponseDto;
import com.kim.customer.entity.Customer;
import com.kim.customer.exception.ResourceNotFoundException;
import com.kim.customer.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
        name = "CRUD REST APIs for Customer",
        description = "CRUD REST APIs in Customer to CREATE, UPDATE, FETCH AND DELETE customer details"
)
@RestController
@RequestMapping(path= "/api" , produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Operation(
            summary = "Create Customer REST API",
            description = "REST API to create new Customer"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        customerService.createCustomer(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details based on a Mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomer(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "MobileNumber must be 10 digits")
                                                         String mobileNumber) {
        CustomerDto customerDto = customerService.fetchCustomer(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }


    @Operation(
            summary = "Update Customer Details REST API",
            description = "REST API to update Customer details based on a Mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try {
            boolean isUpdated = customerService.updateCustomer(customerDto.getMobileNumber(), customerDto);
            if (isUpdated) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
            } else {
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .body(new ResponseDto(CustomerConstants.STATUS_417, CustomerConstants.MESSAGE_417_UPDATE));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto(CustomerConstants.STATUS_404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(CustomerConstants.STATUS_500, CustomerConstants.MESSAGE_500));
        }
    }

    @Operation(
            summary = "Delete Customer Details REST API",
            description = "REST API to delete Customer based on a Mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "MobileNumber must be 10 digits")
                                                          String mobileNumber) {
        try {
            boolean isDeleted = customerService.deleteCustomer(mobileNumber);
            if (isDeleted) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200_DELETE));
            } else {
                return ResponseEntity
                        .status(HttpStatus.EXPECTATION_FAILED)
                        .body(new ResponseDto(CustomerConstants.STATUS_417, CustomerConstants.MESSAGE_417_DELETE));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto(CustomerConstants.STATUS_404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(CustomerConstants.STATUS_500, CustomerConstants.MESSAGE_500));
        }
    }

    @Operation(
            summary = "Fetch All Customers with Pagination and Sorting REST API",
            description = "REST API to fetch all Customer details with pagination and sorting"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/customers")
    public ResponseEntity<Page<CustomerDto>> findAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "customerId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerDto> customers = customerService.findAllCustomers(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @Operation(
            summary = "Search Customer REST API",
            description = "REST API to fetch all Customer details with pagination and sorting"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String searchTerm) {
        return customerService.searchCustomers(searchTerm);
    }


}
