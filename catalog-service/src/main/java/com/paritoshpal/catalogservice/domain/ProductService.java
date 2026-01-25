package com.paritoshpal.catalogservice.domain;

import com.paritoshpal.catalogservice.ApplicationProperties;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationProperties applicationProperties;

    public PageResult<Product> getProducts(int pagenNo) {
        Sort sort = Sort.by("name").ascending();
        pagenNo = pagenNo <= 1 ? 0 : pagenNo - 1;
        Pageable pageable = PageRequest.of(pagenNo, applicationProperties.pageSize(), sort);
        Page<Product> productPage = productRepository.findAll(pageable)
                .map(ProductMapper::toProduct);


        return new PageResult<>(
                productPage.getContent(),
                (int) productPage.getTotalElements(),
                productPage.getNumber() + 1,
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast(),
                productPage.hasNext(),
                productPage.hasPrevious()
        );

    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code)
                .map(ProductMapper::toProduct);
    }

}
