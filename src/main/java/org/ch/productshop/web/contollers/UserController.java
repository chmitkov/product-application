package org.ch.productshop.web.contollers;

import org.ch.productshop.domain.models.binding.UserEditBindingModel;
import org.ch.productshop.domain.models.binding.UserRegisterBindingModel;
import org.ch.productshop.domain.models.service.UserServiceModel;
import org.ch.productshop.domain.models.view.UserAllViewModel;
import org.ch.productshop.domain.models.view.UserProfileViewModel;
import org.ch.productshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register() {
        return super.view("register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@ModelAttribute(name = "model") UserRegisterBindingModel model) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            return super.view("register");
        }

        this.userService
                .registerUser(this.modelMapper.map(model, UserServiceModel.class));

        return super.view("login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login() {
        return super.view("login");
    }


    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {
        UserProfileViewModel userProfileViewModel = this.modelMapper
                .map(this.userService.findUserByUsername(principal.getName()),
                        UserProfileViewModel.class);

        modelAndView.addObject("model", userProfileViewModel);

        return super.view("profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView edit(Principal principal, ModelAndView modelAndView) {
        UserProfileViewModel userProfileViewModel = this.modelMapper
                .map(this.userService.findUserByUsername(principal.getName()),
                        UserProfileViewModel.class);

        modelAndView.addObject("model", userProfileViewModel);

        return super.view("edit-profile", modelAndView);
    }

    @PatchMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute(name = "model") UserEditBindingModel model) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            return super.view("edit-profile");
        }

        this.userService.editUserProfile(
                this.modelMapper.map(model, UserServiceModel.class), model.getOldPassword());

        return super.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView all(ModelAndView modelAndView) {
        List<UserAllViewModel> userAllViewModels = this.userService
                .findAllUsers()
                .stream()
                .map(u -> {
                    UserAllViewModel userAllViewModel = this.modelMapper.map(u, UserAllViewModel.class);
                    userAllViewModel.setAuthorities(
                            u.getAuthorities()
                                    .stream()
                                    .map(a -> a.getAuthority())
                                    .collect(Collectors.toSet()));

                    return userAllViewModel;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("users", userAllViewModels);

        return super.view("all-users", modelAndView);
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id) {
        this.userService.setUserRole(id, "user");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModerator(@PathVariable String id) {
        this.userService.setUserRole(id, "moderator");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id) {
        this.userService.setUserRole(id, "admin");

        return super.redirect("/users/all");
    }

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class,
                new StringTrimmerEditor(true));
    }
}
