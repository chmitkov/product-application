package org.ch.productshop.service;

import org.ch.productshop.domain.models.service.RoleServiceModel;
import org.ch.productshop.domain.models.service.UserServiceModel;

import java.util.Set;

public interface RoleService  {

    void seedRolesInDB();

    //void assignUserRoles(UserServiceModel userServiceModel, long numbersOfUsers);

    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}
