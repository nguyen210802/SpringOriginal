package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.entity.Address;
import com.example.identityService.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressServiceImpl;

    @GetMapping("/{addressId}")
    public ApiResponse<Address> getAddress(@PathVariable("addressId") String addressId){
        return ApiResponse.<Address>builder()
                .result(addressServiceImpl.getAddressById(addressId))
                .build();
    }

    @GetMapping("/getByBuyer")
    public ApiResponse<List<Address>> getAllByUser(){
        return ApiResponse.<List<Address>>builder()
                .result(addressServiceImpl.getAllByBuyer())
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<Address> createAddress(@RequestBody Address address){
        return ApiResponse.<Address>builder()
                .result(addressServiceImpl.create(address))
                .build();
    }

    @PutMapping("/update/{addressId}")
    public ApiResponse<Address> updateAddress(@PathVariable("addressId") String addressId, @RequestBody Address updatedAddress){
        return ApiResponse.<Address>builder()
                .result(addressServiceImpl.update(addressId, updatedAddress))
                .build();
    }

    @DeleteMapping("/delete/{addressId}")
    public ApiResponse<String> deleteAddress(@PathVariable("addressId") String addressId){
        return ApiResponse.<String>builder()
               .result(addressServiceImpl.delete(addressId))
               .build();
    }
}
