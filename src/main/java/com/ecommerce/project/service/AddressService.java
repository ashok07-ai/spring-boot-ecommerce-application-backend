package com.ecommerce.project.service;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.DTO.AddressDTO;
import com.ecommerce.project.payload.response.AddressResponse;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User loggedInUser);

    AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    String deleteAddress(Long addressId);
}
