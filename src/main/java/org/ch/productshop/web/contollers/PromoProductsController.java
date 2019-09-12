package org.ch.productshop.web.contollers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/promo")
public class PromoProductsController extends BaseController{

    @GetMapping("/allPromotions")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView showAllPromotions(){
        return view("promo/promo");
    }
}
