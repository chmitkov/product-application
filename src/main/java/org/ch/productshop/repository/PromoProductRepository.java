package org.ch.productshop.repository;

import org.ch.productshop.domain.entities.PromoProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoProductRepository extends JpaRepository<PromoProduct, String> {
}
