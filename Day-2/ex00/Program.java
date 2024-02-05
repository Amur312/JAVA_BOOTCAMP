package ex00;

public class Program {
    public static void main(String[] args) {
        try {
            FileSignatures fileSignatures = new FileSignatures();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
