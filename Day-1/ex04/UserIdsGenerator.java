package ex04;

public class UserIdsGenerator {
    private static UserIdsGenerator instance;
    private int lastGeneratedId;
    private UserIdsGenerator(){
        lastGeneratedId = 0;
    }
    public static UserIdsGenerator getInstance(){
        if(instance == null){
            instance = new UserIdsGenerator();
        }
        return instance;
    }
    public int generateId(){
        return ++lastGeneratedId;
    }
}
