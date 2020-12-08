package tools;

public class ExpiredTokenException extends Throwable {
    public ExpiredTokenException(String expiredToken) {
        super(expiredToken);
    }
}
