package lesson3;

class MyThread implements Runnable{
    private int seat=50;
    @Override
    public void run() {
        for(;seat>0;){
            synchronized (this){
                if(this.seat>0){
                    seat--;
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
public class SynchronizedTest2 {
    /*有一个教室 50个座位 同时有三个老师安排同学的座位
    * 每个老师安排100个同学到这个教室 模拟使用多线程实现
    * 座位编号1-50 三个线程同时启动安排同学 直到座位满
    * */
    public static void main(String[] args) {
        MyThread mt=new MyThread();
        Thread t1=new Thread(mt,"TeacherA");
        Thread t2=new Thread(mt,"TeacherB");
        Thread t3=new Thread(mt,"TeacherC");
        t1.start();
        t2.start();
        t3.start();
    }
}
