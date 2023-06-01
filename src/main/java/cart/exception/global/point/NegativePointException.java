package cart.exception.global.point;

import cart.exception.global.GlobalException;

public class NegativePointException extends GlobalException {

    private static final String message = "포인트는 음수값일 수 없습니다.";

    public NegativePointException() {
        super(message);
    }
}