package org.ch.productshop.web.contollers;

import org.ch.productshop.domain.models.binding.ProductAddBindingModel;
import org.ch.productshop.domain.models.service.CategoryServiceModel;
import org.ch.productshop.domain.models.service.ProductServiceModel;
import org.ch.productshop.domain.models.view.ProductAllViewModel;
import org.ch.productshop.domain.models.view.ProductDetailsViewModel;
import org.ch.productshop.domain.models.view.ProductViewModel;
import org.ch.productshop.error.ProductNotFoundException;
import org.ch.productshop.service.CategoryService;
import org.ch.productshop.service.CloudinaryService;
import org.ch.productshop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }


    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProduct() {
        return super.view("product/add-product");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProductConfirm(@ModelAttribute(name = "model") ProductAddBindingModel model) throws Exception {
        ProductServiceModel productServiceModel = this.modelMapper
                .map(model, ProductServiceModel.class);

        productServiceModel.setCategories(
                this.categoryService
                        .findAllCategories()
                        .stream()
                        .filter(c -> model.getCategories().contains(c.getId()))
                        .collect(Collectors.toList())
        );

        productServiceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImage()));

        this.productService.addProduct(productServiceModel);

        return super.redirect("/products/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView allProducts(@ModelAttribute(name = "model") ProductAddBindingModel model,
                                    ModelAndView modelAndView) {

        List<ProductAllViewModel> products = this.productService
                .findAllProducts()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductAllViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("products", products);

        return super.view("product/all-products", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView modelAndView) {
        ProductDetailsViewModel productDetailsViewModel = this.modelMapper
                .map(this.productService.findProductById(id), ProductDetailsViewModel.class);

        modelAndView.addObject("product", productDetailsViewModel);

        return super.view("product/details-product", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView modelAndView) {

        ProductViewModel productViewModel = this.modelMapper
                .map(this.productService.findProductById(id), ProductViewModel.class);

        modelAndView.addObject("product", productViewModel);

        return super.view("/product/edit-product", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editProductConfirm(@PathVariable String id, @ModelAttribute ProductAddBindingModel productAddBindingModel) {
        ProductServiceModel productServiceModel = this.modelMapper.map(productAddBindingModel, ProductServiceModel.class);
        productServiceModel.setCategories(productAddBindingModel
                .getCategories()
                .stream()
                .map(this.categoryService::findById)
                .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
                .collect(Collectors.toList())
        );

        this.productService
                .editProduct(id, productServiceModel);

        return super.redirect("/products/details/" + id);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView modelAndView) {
        ProductViewModel productViewModel = this.modelMapper
                .map(this.productService.findProductById(id), ProductViewModel.class);

        modelAndView.addObject("product", productViewModel);

        return super.view("product/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteProductConfirm(@PathVariable String id) {
        this.productService
                .deleteProduct(id);

        return super.redirect("/products/all");
    }

    @GetMapping("/fetch/{category}")
    @ResponseBody
    public List<ProductAllViewModel> fetchByCategory(@PathVariable String category) {
        if (category.equals("all")) {
            return this.productService.findAllProducts()
                    .stream()
                    .map(product -> this.modelMapper.map(product, ProductAllViewModel.class))
                    .collect(Collectors.toList());
        }

        return this.productService.findAllProductsByCategory(category)
                .stream()
                .map(product -> this.modelMapper.map(product, ProductAllViewModel.class))
                .collect(Collectors.toList());
    }

//    @ExceptionHandler({ProductNotFoundException.class})
//    public ModelAndView handleNotFoundException(ProductNotFoundException e) {
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("message", e.getMessage());
//        modelAndView.addObject("statusCode", e.getStatusCode());
//
//        return modelAndView;
//    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class,
                new StringTrimmerEditor(true));
    }
}
