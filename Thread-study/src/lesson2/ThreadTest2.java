package lesson2;

public class ThreadTest2 {
    public static void main(String[] args) throws InterruptedException {
        //main主线程和子线程同时执行
        /*for(int i=0;i<20;i++){
            final int n=i;
            //new Thread()操作有点耗时
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {//内部类使用外部的变量，必须是final修饰
                    System.out.println(n);
                }
            });
            t.start();
        }
        System.out.println("ok");*/
        Thread[] threads=new Thread[20];
        for(int i=0;i<20;i++) {
            final int n = i;
            //new Thread()操作有点耗时c
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {//内部类使用外部的变量，必须是final修饰
                    System.out.println(n);
                }
            });
        }
        for(Thread t:threads) {
            t.start();
            //t.join();
        }
            /*while(Thread.activeCount()>1){//实际工作中不会这么用 简单满足功能：子线程执行完在执行主线程代码
                Thread.yield();//让当前线程让步：从运行态转变为就绪态
            }*/
        for(Thread t:threads){//同时执行20个线程，再等待所有线程执行完毕
            t.join();
        }
        System.out.println("ok");
    }
}
