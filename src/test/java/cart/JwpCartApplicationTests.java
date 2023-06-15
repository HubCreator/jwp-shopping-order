package cart;

import cart.config.InitData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Rollback(value = false)
class JwpCartApplicationTests {

    @Autowired
    InitData initData;

    @Test
    void contextLoads() {
        initData.run();
    }
}
