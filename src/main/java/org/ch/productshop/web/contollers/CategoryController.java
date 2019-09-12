package org.ch.productshop.web.contollers;


import org.ch.productshop.domain.models.binding.CategoryAddBindingModel;
import org.ch.productshop.domain.models.service.CategoryServiceModel;
import org.ch.productshop.domain.models.view.CategoryAllViewModel;
import org.ch.productshop.domain.models.view.CategoryDeleteViewModel;
import org.ch.productshop.domain.models.view.CategoryEditViewModel;
import org.ch.productshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addCategory() {
        return super.view("category/add-category");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addCategoryConfirm(@ModelAttribute(name = "model") CategoryAddBindingModel model) {
        this.categoryService
                .addCategory(this.modelMapper.map(model, CategoryServiceModel.class));

        return super.redirect("/categories/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView allCategory(ModelAndView modelAndView) {
        List<CategoryAllViewModel> categories = this.categoryService
                .findAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryAllViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("categories", categories);

        return super.view("category/all-categories", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editCategory(@PathVariable String id, ModelAndView modelAndView) {
        CategoryEditViewModel categoryEditViewModel = this.modelMapper
                .map(this.categoryService.findById(id), CategoryEditViewModel.class);

        modelAndView.addObject("category", categoryEditViewModel);

        return super.view("category/edit-category", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editCategoryConfirm(@PathVariable String id, @ModelAttribute(name = "model")
            CategoryAddBindingModel model) {

        this.categoryService.editCategory(id, this.modelMapper.map(model, CategoryServiceModel.class));

        return super.redirect("/categories/all");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteCategory(@PathVariable String id, ModelAndView modelAndView) {
        CategoryDeleteViewModel categoryDeleteViewModel = this.modelMapper
                .map(this.categoryService.findById(id), CategoryDeleteViewModel.class);

        modelAndView.addObject("category", categoryDeleteViewModel);

        return super.view("/category/delete-category", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteCategoryConfirm(@PathVariable String id) {
        this.categoryService
                .deleteCategory(id);

        return super.redirect("/categories/all");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<CategoryAllViewModel> fetchCategories() {
        return this.categoryService
                .findAllCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryAllViewModel.class))
                .collect(Collectors.toList());
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class,
                new StringTrimmerEditor(true));
    }
}
