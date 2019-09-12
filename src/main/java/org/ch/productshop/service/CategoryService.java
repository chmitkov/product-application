package org.ch.productshop.service;

import org.ch.productshop.domain.models.service.CategoryServiceModel;
import org.ch.productshop.error.CategoryInvalidNameException;

import java.util.List;

public interface CategoryService {

    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) throws CategoryInvalidNameException;

    List<CategoryServiceModel> findAllCategories();

    CategoryServiceModel findById(String id);

    CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel);

    void deleteCategory(String id);
}
