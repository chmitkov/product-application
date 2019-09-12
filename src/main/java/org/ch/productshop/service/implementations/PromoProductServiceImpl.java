package org.ch.productshop.service.implementations;

import org.ch.productshop.domain.entities.Product;
import org.ch.productshop.domain.entities.PromoProduct;
import org.ch.productshop.domain.models.service.ProductServiceModel;
import org.ch.productshop.domain.models.service.PromoProductServiceModel;
import org.ch.productshop.repository.PromoProductRepository;
import org.ch.productshop.service.ProductService;
import org.ch.productshop.service.PromoProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PromoProductServiceImpl implements PromoProductService {

    private final PromoProductRepository promoProductRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public PromoProductServiceImpl(PromoProductRepository promoProductRepository, ProductService productService, ModelMapper modelMapper) {
        this.promoProductRepository = promoProductRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<PromoProductServiceModel> findAll() {
        return this.promoProductRepository
                .findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, PromoProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addPromoProduct(PromoProductServiceModel promoProductServiceModel) {
        PromoProduct promoProduct = this.modelMapper
                .map(promoProductServiceModel, PromoProduct.class);

        this.promoProductRepository
                .save(promoProduct);
    }

    @Override
    public void addRandomPromoProduct() {

        List<ProductServiceModel> allProducts = this.productService
                .findAllProducts();

        int randomIndex = new Random().nextInt(allProducts.size());
        int randomDiscount = new Random().nextInt(50);
        ProductServiceModel productServiceModel;
        try {
            productServiceModel = allProducts.get(randomIndex);
        } catch (Exception e) {
            productServiceModel = allProducts.get(0);
        }

        Product product = this.modelMapper.map(productServiceModel, Product.class);

        PromoProductServiceModel promoProductServiceModel = new PromoProductServiceModel();
        promoProductServiceModel.setProduct(product);
        promoProductServiceModel.setDiscount(randomDiscount);
        promoProductServiceModel.setDiscountedPrice(
                product.getPrice().subtract(
                        product.getPrice().multiply(
                                BigDecimal.valueOf(0.01 * randomDiscount))));

        this.addPromoProduct(promoProductServiceModel);
    }

    //100000 milliseconds
    @Scheduled(fixedRate = 10000)
    private void generateRandomPromoProduct() {
        this.addRandomPromoProduct();
    }

    @Scheduled(fixedRate = 100000)
    private void clearAllPromosFromDb() {
        this.promoProductRepository
                .deleteAll();
    }
}
