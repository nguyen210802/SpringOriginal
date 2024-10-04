package com.example.identityService.service.impl;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.ProductRequest;
import com.example.identityService.entity.Product;
import com.example.identityService.entity.ProductImage;
import com.example.identityService.exception.AppException;
import com.example.identityService.exception.ErrorCode;
import com.example.identityService.repository.ProductImageRepository;
import com.example.identityService.repository.ProductRepository;
import com.example.identityService.repository.UserRepository;
import com.example.identityService.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImpl implements ProductService {
    UserRepository userRepository;
    ProductRepository productRepository;
    ProductImageRepository productImageRepository;

    @Override
    public PageResponse<Product> getAll(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = productRepository.findAll(pageable);
        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public PageResponse<Product> getAllMyProduct(int page, int size) {
        Authentication authenticated = SecurityContextHolder.getContext().getAuthentication();
        String sellerId = authenticated.getName();

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createAt").descending());
        var pageData = productRepository.findAllBySeller_Id(sellerId, pageable);

        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public PageResponse<Product> getAllByName(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = productRepository.findByNameContainingIgnoreCase(name, pageable);

//        List<Product> product = productRepository.findAll();
//        log.info("Product Image: {}", product.get(0).getImages().get(0));

        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

    @Override
    @Transactional
    public Product create(ProductRequest request) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String sellerId = authenticated.getName();
        List<ProductImage> hashSetImages = new ArrayList<>();
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .manufacturer(request.getManufacturer())
                .seller(userRepository.findById(sellerId).orElseThrow())
                .build();
        product = productRepository.save(product);

        if (request.getImages() != null && request.getImages().length > 0) {
            List<byte[]> imageList = new ArrayList<>();
            for (String base64Image : request.getImages()) {
                // Loại bỏ phần đầu của chuỗi base64 nếu có
                if (base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                }
                imageList.add(Base64.getDecoder().decode(base64Image));
            }

            boolean isFirstImage = true;
            for (byte[] image : imageList) {
                ProductImage productImage = productImageRepository.save(ProductImage.builder()
                       .product(product)
                       .image(image)
                       .mainImage(isFirstImage)
                       .build());
                isFirstImage = false;

                hashSetImages.add(productImage);
            }
        }

        product.setImages(hashSetImages);

        return productRepository.save(product);
    }

    @Override
    public Product update(String id, Product update) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();

        Product product = productRepository.findById(id).orElseThrow(
                () ->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if(authenticated.getName().equals(product.getSeller().getId())){
            product.setName(update.getName());
            product.setImages(update.getImages());
            product.setDescription(update.getDescription());
            product.setPrice(update.getPrice());
            return productRepository.save(product);
        }
        else{
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public String delete(String id) {
        var authenticated = SecurityContextHolder.getContext().getAuthentication();
        String sellerId = authenticated.getName();
        String authentication = authenticated.getAuthorities().toString();
        log.info("authentication: {}", authentication);

        Product product = productRepository.findById(id).orElseThrow(
                () ->new IllegalStateException("Product not exited"));

        if(authentication.equals("ROLE_ADMIN") || sellerId.equals(product.getSeller().getId())){
            productRepository.deleteById(id);
        }
        else{
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return "delete successfully";
    }
}
