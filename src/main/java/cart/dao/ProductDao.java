package cart.dao;

import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource,
                      final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingColumns("name", "price", "image_url")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final ProductName name = new ProductName(rs.getString("name"));
        final ProductPrice price = new ProductPrice(rs.getInt("price"));
        final ProductImageUrl imageUrl = new ProductImageUrl(rs.getString("image_url"));

        return new Product(id, name, price, imageUrl);
    };

    public Long insert(final Product product) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", product.getNameValue())
                .addValue("price", product.getPriceValue())
                .addValue("image_url", product.getImageUrlValue());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Product> findAll() {
        final String sql = "SELECT id, name, price, image_url FROM product";

        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Optional<Product> findById(final Long id) {
        final String sql = "SELECT id, name, price, image_url FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, productRowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Product> findAllByIds(final List<Long> ids) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        final String sql = "SELECT id, name, price, image_url " +
                "FROM product " +
                "WHERE id IN (:ids)";

        return namedParameterJdbcTemplate.query(sql, parameters, productRowMapper);
    }

    public void update(Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";

        jdbcTemplate.update(sql, product.getNameValue(), product.getPriceValue(), product.getImageUrlValue(), product.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM product WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
