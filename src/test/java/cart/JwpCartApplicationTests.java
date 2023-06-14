package cart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
class JwpCartApplicationTests {

    @Sql("/data.sql")
    @Test
    void contextLoads() {
    }
}
