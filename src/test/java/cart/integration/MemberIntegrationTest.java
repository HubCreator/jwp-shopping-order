package cart.integration;

import cart.domain.member.Member;
import cart.repository.datajpa.MemberDataJpaRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;

public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDataJpaRepository memberRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @DisplayName("유저가 가지고 있는 포인트를 확인한다.")
    @Test
    void getPoint() {
        // given
        final Member member = memberRepository.findById(1L).orElseThrow();

        // when, then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .when()
                .get("/members/point")
                .then().log().all()
                .body("point", is(10000));
    }
}
