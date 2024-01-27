package ex04;

public class CustomUserNotFoundException  extends RuntimeException{
    public CustomUserNotFoundException(String message) {
        super(message);
    }
}
