package org.ch.productshop.web.contollers;

import org.ch.productshop.domain.models.view.PromoProductViewModel;
import org.ch.productshop.service.PromoProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promo")
public class PromoProductsRestController {

    private final PromoProductService promoProductService;
    private final ModelMapper modelMapper;

    @Autowired
    public PromoProductsRestController(PromoProductService promoProductService, ModelMapper modelMapper) {
        this.promoProductService = promoProductService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<PromoProductViewModel> findAllPromoProducts() {
        return this.promoProductService
                .findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, PromoProductViewModel.class))
                .collect(Collectors.toList());
    }
}
