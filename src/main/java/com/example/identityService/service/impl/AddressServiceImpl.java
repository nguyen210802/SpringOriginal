package com.example.identityService.service.impl;

import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Address;
import com.example.identityService.entity.User;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.AddressRepository;
import com.example.identityService.repository.UserRepository;
import com.example.identityService.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressServiceImpl implements AddressService {
    UserRepository userRepository;
    AddressRepository addressRepository;

    @Override
    public Address getAddressById(String addressId) {
        String buyerId = getMyInfo().getId();

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("Address not fund!"));
        if(!address.getBuyer().getId().equals(buyerId))
            throw new AppException(ErrorCode.UNAUTHORIZED);
        return address;
    }

    @Override
    public List<Address> getAllByBuyer() {
        String buyerId = getMyInfo().getId();
        return addressRepository.findAllByBuyer_Id(buyerId);
    }

    @Override
    public Address create(Address address) {
        address.setBuyer(getMyInfo());

        return addressRepository.save(address);
    }

    @Override
    public Address update(String addressId, Address address) {
        Address addressOld = getAddressById(addressId);

        addressOld.setCity(address.getCity());
        addressOld.setDistrict(address.getDistrict());
        addressOld.setCommune(address.getCommune());
        addressOld.setStreet(address.getCommune());

        return addressRepository.save(addressOld);
    }

    @Override
    public String delete(String addressId) {
        Address address = getAddressById(addressId);

        addressRepository.delete(address);

        return "Delete successfully";
    }

    public User getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        return userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
    }
}
