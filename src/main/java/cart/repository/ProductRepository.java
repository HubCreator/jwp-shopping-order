package cart.repository;

import cart.domain.product.Product;
import cart.exception.notfound.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final EntityManager em;

    public void save(final Product product) {
        em.persist(product);
    }

    public Product findOne(final Long id) {
        final Product product = em.find(Product.class, id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    public List<Product> findAllByIds(final List<Long> ids) {
        return em.createQuery("select p from Product p where p.id in :ids", Product.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    public void updateProduct(final Product product) {
        final Product findProduct = findOne(product.getId());
        findProduct.updateTo(product);
    }

    public void deleteById(final Long id) {
        final Product product = findOne(id);
        em.remove(product);
    }
}
