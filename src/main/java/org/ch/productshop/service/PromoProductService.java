package org.ch.productshop.service;

import org.ch.productshop.domain.models.service.PromoProductServiceModel;

import java.util.List;

public interface PromoProductService {
    void addPromoProduct(PromoProductServiceModel promoProductServiceModel);

    void addRandomPromoProduct();

    List<PromoProductServiceModel> findAll();
}
