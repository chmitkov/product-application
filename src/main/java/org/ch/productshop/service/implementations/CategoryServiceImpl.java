package org.ch.productshop.service.implementations;

import org.ch.productshop.domain.entities.Category;
import org.ch.productshop.domain.models.service.CategoryServiceModel;
import org.ch.productshop.error.CategoryInvalidNameException;
import org.ch.productshop.repository.CategoryRepository;
import org.ch.productshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, Validator validator) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }


    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) throws CategoryInvalidNameException {

        if(!this.validator.validate(categoryServiceModel).isEmpty()){
            throw new CategoryInvalidNameException("Invalid category name!");
        }

        this.categoryRepository
                .saveAndFlush(this.modelMapper
                        .map(categoryServiceModel, Category.class));

        return categoryServiceModel;
    }

    @Override
    public List<CategoryServiceModel> findAllCategories() {
        return this.categoryRepository
                .findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryServiceModel findById(String id) {
        return this.categoryRepository
                .findById(id)
                .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel) {
        Category category = this.categoryRepository
                .findById(id)
                .orElseThrow(IllegalArgumentException::new);

        category.setName(categoryServiceModel.getName());

        return this.modelMapper
                .map(this.categoryRepository.saveAndFlush(category),
                        CategoryServiceModel.class);
    }

    @Override
    public void deleteCategory(String id) {
        this.categoryRepository
                .deleteById(id);
    }
}
