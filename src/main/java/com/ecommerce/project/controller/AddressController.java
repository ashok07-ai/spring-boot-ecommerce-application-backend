package com.ecommerce.project.controller;

import com.ecommerce.project.constants.AppConstants;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.DTO.AddressDTO;
import com.ecommerce.project.payload.DTO.ProductDTO;
import com.ecommerce.project.payload.response.AddressResponse;
import com.ecommerce.project.payload.response.ProductResponse;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User loggedInUser = authUtil.loggedInUser();
        AddressDTO saveAddress = addressService.createAddress(addressDTO, loggedInUser);
        return new ResponseEntity<AddressDTO>(saveAddress, HttpStatus.CREATED);
    }

    @GetMapping("/public/addresses")
    public ResponseEntity<AddressResponse> getAllAddresses(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ADDRESSES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder
    ){
        AddressResponse addressResponse =  addressService.getAllAddresses(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<AddressResponse>(addressResponse, HttpStatus.OK);
    }

    @DeleteMapping("/admin/address/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
