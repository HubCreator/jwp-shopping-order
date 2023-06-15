package cart.repository;

import cart.domain.product.Product;
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

        /*if (product.getId() == null) {
            em.persist(product);
            return;
        }
        em.merge(product);*/
    }

    public Product findOne(final Long productId) {
        return em.find(Product.class, productId);
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

    public void delete(final Long productId) {
        final Product findProduct = findOne(productId);
        em.remove(findProduct);
    }
}
