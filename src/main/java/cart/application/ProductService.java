package cart.application;

import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.repository.ProductRepository;
import cart.ui.dto.product.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long createProduct(final ProductRequest request) {
        final Product product = new Product(
                new ProductName(request.getName()),
                new ProductPrice(request.getPrice()),
                new ProductImageUrl(request.getImageUrl()));
        productRepository.save(product);
        return product.getId();
    }

    public Product getProductById(final Long productId) {
        return productRepository.findOne(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest request) {
        final Product product = new Product(
                productId,
                new ProductName(request.getName()),
                new ProductPrice(request.getPrice()),
                new ProductImageUrl(request.getImageUrl()));
        productRepository.updateProduct(product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productRepository.delete(productId);
    }
}
