package src.ex01;

public class HenThread extends Thread{
    private final int count;

    public HenThread(int count) {
        this.count = count;
    }

    @Override
    public void run(){
        for (int i = 0; i < count; i++) {
            synchronized (Program.lock){
                System.out.println("Hen");
                Program.lock.notify();
                try {
                    if (i < count - 1) {
                        Program.lock.wait();
                    }
                }catch (InterruptedException e ){
                    e.printStackTrace();
                }
            }
        }
    }
}
