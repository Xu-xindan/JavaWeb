package lesson6;

public class notifyTest {
    public static void main(String[] args) throws InterruptedException {
        //竞争class对象锁
        synchronized (notifyTest.class){
            //当前线程释放对象锁
            notifyTest.class.wait();
            //通知调用同一个对象的wait()阻塞的线程（唤醒），唤醒后竞争对象锁
            notifyTest.class.notify();
        }//释放对象锁
    }
}
