package cart.exception.notfound;

public abstract class NotFoundException extends RuntimeException {

    private final String message;

    public NotFoundException(final String message) {
        this.message = message;
    }

    public NotFoundException(final String message, final Throwable cause) {
        this.message = message;
        cause.printStackTrace();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
