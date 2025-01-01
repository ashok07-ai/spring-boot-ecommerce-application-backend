package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.DTO.AddressDTO;
import com.ecommerce.project.payload.DTO.ProductDTO;
import com.ecommerce.project.payload.response.AddressResponse;
import com.ecommerce.project.payload.response.ProductResponse;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User loggedInUser) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = loggedInUser.getAddresses();
        addressList.add(address);
        loggedInUser.setAddresses(addressList);
        address.setUser(loggedInUser);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);

    }

    @Override
    public AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Address> addressPage = addressRepository.findAll(pageDetails);
        List<Address> addresses = addressPage.getContent();
        if(addresses.isEmpty()){
            throw new APIException("No addresses found!");
        }
        List<AddressDTO> adddressDtos = addresses.stream().map(product -> modelMapper.map(addresses, AddressDTO.class))
                .toList();
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setResponseObject(adddressDtos);
        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setTotalElements(addressPage.getTotalElements());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setLastPage(addressPage.isLast());
        return addressResponse;
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressDetails = addressRepository.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));
        // DELETE
        User user = addressDetails.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressDetails));
        userRepository.save(user);
         addressRepository.delete(addressDetails);
        return "Address deleted successfully!";
    }
}
