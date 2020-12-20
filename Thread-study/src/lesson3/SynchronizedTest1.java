package lesson3;

public class SynchronizedTest1 {

    private static int COUNT;

    //对当前类对象进行加锁，线程间同步互斥
    public synchronized  void increment(){
        COUNT++;
    }

    //使用不同的对象加锁，没有同步互斥的作用：并发并行
//    public static void increment(){
//        synchronized (new SynchronizedTest()){
//            COUNT++;
//        }
//    }

    //有一个变量COUNT=0;同时启动20个线程，每个线程循环1000次，每次循环COUNT++
    //等着20个子线程执行完毕之后，在main线程打印COUNT（预期20000）
    public static void main(String[] args) throws InterruptedException {
        Class clazz = SynchronizedTest1.class;
        SynchronizedTest1 st=new SynchronizedTest1();
        //尽量同时启动，不让new Thread耗时影响
        Thread[] threads = new Thread[20];
        for(int i=0; i<19; i++){
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0; j<1000; j++){
                        /*synchronized (SynchronizedTest1.class) {
                            COUNT++;
                        }*/
                        //increment();//静态同步方法
                        //st.increment();//实力同步方法
                        synchronized (st){
                            COUNT++;
                        }
                    }
                }
            });
        }

        for(int i=0; i<1; i++){
            threads[19+i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0; j<1000; j++){
                        //increment();
                        /*synchronized (SynchronizedTest1.class) {
                            COUNT++;
                        }*/
                        st.increment();
                    }
                }
            });
        }
        for(Thread t : threads){
            if(t != null)
                t.start();
        }
        //让main线程阻塞等待所有的20个子线程执行完毕
        for(Thread t : threads){
            if(t != null)
                t.join();
        }
        System.out.println(COUNT);
    }
}
