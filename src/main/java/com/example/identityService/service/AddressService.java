package com.example.identityService.service;

import com.example.identityService.entity.Address;

import java.util.List;

public interface AddressService {
    Address getAddressById(String addressId);
    List<Address> getAllByBuyer();
    Address create(Address address);
    Address update(String addressId, Address address);
    String delete(String addressId);
}
