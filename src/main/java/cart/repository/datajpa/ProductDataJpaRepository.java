package cart.repository.datajpa;

import cart.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDataJpaRepository extends JpaRepository<Product, Long> {

}
