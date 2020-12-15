package lesson2;

public class SleepTest {
    public static void main(String[] args) throws InterruptedException{
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(99999999);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
        while(true){

        }
    }
}
