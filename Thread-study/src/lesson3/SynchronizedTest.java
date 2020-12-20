package lesson3;

public class SynchronizedTest {
    private static int COUNT;

    //对当前类对象进行加锁 线程间同步互斥
    /*private static synchronized void increment(){//直接方法加锁 后面调用就加锁
        COUNT++;//20000
    }*/

    //使用不同的对象加锁 没有同步互斥效果
    private static synchronized void increment(){//不安全
        synchronized (new SynchronizedTest()) {
            COUNT++;//<20000
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (SynchronizedTest.class){
                    increment();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                increment();
            }
        }).start();
    }
}
