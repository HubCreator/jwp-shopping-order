package cart.exception.business.order;

import cart.domain.product.ProductPrice;
import cart.exception.business.BusinessException;

public class InvalidPointUseException extends BusinessException {

    private static final String MESSAGE = "상품 가격보다 더 많은 포인트를 입력하실 수 없습니다. 상품 가격: %d, 입력한 포인트 : %d";

    public InvalidPointUseException(final ProductPrice productPrice, final Integer inputPoint) {
        super(String.format(MESSAGE, productPrice.getPrice(), inputPoint));
    }
}
