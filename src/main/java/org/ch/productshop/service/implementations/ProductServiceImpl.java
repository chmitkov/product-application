package org.ch.productshop.service.implementations;

import org.ch.productshop.domain.entities.Category;
import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.domain.models.service.ProductServiceModel;
import org.ch.productshop.error.ProductNotFoundException;
import org.ch.productshop.repository.ProductRepository;
import org.ch.productshop.service.CategoryService;
import org.ch.productshop.service.ProductService;
import org.ch.productshop.validations.ProductValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ProductValidationService productValidationService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ModelMapper modelMapper, ProductValidationService productValidationService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.productValidationService = productValidationService;
    }


    @Override
    public ProductServiceModel addProduct(ProductServiceModel productServiceModel) throws Exception {
        Product product = this.modelMapper
                .map(productServiceModel, Product.class);

        if (!this.productValidationService.isValid(product)) {
            throw new IllegalArgumentException();
        }

        this.productRepository.save(product);

        return productServiceModel;
    }

    @Override
    public List<ProductServiceModel> findAllProducts() {
        return this.productRepository
                .findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel findProductById(String id) throws ProductNotFoundException {
        return this.productRepository
                .findById(id)
                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
                .orElseThrow(() ->
                        new ProductNotFoundException("Product with the given id was not found!"));
    }

    @Override
    public ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel) throws ProductNotFoundException {
        Product product = this.productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product with the given id was not found!"));

//        productServiceModel.setCategories(
//                this.categoryService
//                        .findAllCategories()
//                        .stream()
//                        .filter(c -> productServiceModel.getCategories().contains(c.getId()))
//                        .collect(Collectors.toList())
//        );

        product.setName(productServiceModel.getName());
        product.setDescription(productServiceModel.getDescription());
        product.setPrice(productServiceModel.getPrice());
        product.setCategories(
                productServiceModel.getCategories()
                        .stream()
                        .map(c -> this.modelMapper.map(c, Category.class))
                        .collect(Collectors.toList())
        );

        return this.modelMapper.map(this.productRepository.saveAndFlush(product), ProductServiceModel.class);
    }

    @Override
    public void deleteProduct(String id) {
        this.productRepository
                .deleteById(id);
    }

    @Override
    public List<ProductServiceModel> findAllProductsByCategory(String category) {
//        return this.productRepository
//                .findAllByCategoriesContains(category)
//                .stream()
//                .map(p -> this.modelMapper.map(p, ProductServiceModel.class))
//                .collect(Collectors.toList());
        return this.productRepository.findAll()
                .stream()
                .filter(product -> product.getCategories().stream().anyMatch(categoryStream -> categoryStream.getName().equals(category)))
                .map(product -> this.modelMapper.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());

    }


}
