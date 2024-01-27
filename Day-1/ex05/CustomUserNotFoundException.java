package ex05;

public class CustomUserNotFoundException  extends RuntimeException{
    public CustomUserNotFoundException(String message) {
        super(message);
    }
}
