package cart.dao;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("orders")
                .usingColumns("member_id", "used_point")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Order> orderMapper = (result, count) -> {
        final Member member = new Member(
                result.getLong("member.id"),
                result.getString("email"),
                null,
                result.getInt("point"));

        return new Order(
                result.getLong("orders.id"),
                member,
                new MemberPoint(result.getInt("used_point")),
                result.getTimestamp("orders.created_at").toLocalDateTime());
    };

    public Long insert(final Order order) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", order.getMemberId())
                .addValue("used_point", order.getUsedPointValue());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<Order> findById(final Long id) {
        final String sql = "SELECT o.id, o.used_point, o.created_at, " +
                "m.id, m.email, m.point " +
                "FROM orders o " +
                "JOIN member m ON m.id = o.member_id " +
                "WHERE o.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, orderMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT o.id, o.used_point, o.created_at, " +
                "m.id, m.email, m.point " +
                "FROM orders o " +
                "JOIN member m ON m.id = o.member_id " +
                "WHERE o.member_id = ?";

        return jdbcTemplate.query(sql, orderMapper, memberId);
    }
}
