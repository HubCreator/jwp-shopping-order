package cart.integration;

import cart.application.dto.order.OrderRequest;
import cart.domain.member.Member;
import cart.repository.MemberRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1000);

    @DisplayName("장바구니에 들어 있는 상품을 주문할 수 있다.")
    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberRepository.findOne(1L);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .body(orderRequest).log().all()
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/2");
    }

    @DisplayName("주문 id와 연관된 ProductItem 정보를 확인할 수 있다.")
    @Test
    void orderAndGetOrderDetail() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .when().get("/orders/2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("단일 주문에 대한 주문 상품들을 볼 수 있다.")
    @Test
    void getSingleOrderDetail() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .when().get("/orders/2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("orderId", is(2))
                .body("totalPrice", is(100_000))
                .body("deliveryFee", is(0))
                .body("usedPoint", is(1_000))
                .body("products", hasSize(2))
                .body("products[0].product.id", is(3))
                .body("products[1].product.id", is(4));
    }

    @DisplayName("사용자가 주문한 모든 주문 내역을 확인할 수 있다.")
    @Test
    void getAllOrderDetail() {
        final OrderRequest orderRequest = new OrderRequest(List.of(3L, 4L, 5L), 0);
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .body(orderRequest).log().all()
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/3");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .when().get("/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[1].orderId", is(2))
                .body("[1].totalPrice", is(100_000))
                .body("[1].deliveryFee", is(0))
                .body("[1].usedPoint", is(1_000))
                .body("[1].orderedAt", notNullValue())
                .body("[1].products", hasSize(2))
                .body("[1].products[0].product.id", is(3))
                .body("[1].products[1].product.id", is(4))

                .body("[2].orderId", is(3))
                .body("[2].totalPrice", is(82_000))
                .body("[2].deliveryFee", is(0))
                .body("[2].usedPoint", is(0))
                .body("[2].orderedAt", notNullValue())
                .body("[2].products", hasSize(3))
                .body("[2].products[0].product.id", is(5))
                .body("[2].products[1].product.id", is(6))
                .body("[2].products[2].product.id", is(7));
    }
}
