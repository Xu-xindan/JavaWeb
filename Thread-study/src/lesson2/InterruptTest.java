package lesson2;

public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                //执行任务 执行时间可能比较长
                try {
                    for(int i=0;i<10000&&!Thread.currentThread().isInterrupted();i++) {
                        System.out.println(i);
                        //模拟中断线程
                        Thread.sleep(1000);
                        //通过标志位自行实现，无法解决线程阻塞导致无法中断
                        //Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();//线程启动 中断标志位=false
        System.out.println("t start");
        //模拟t执行了5秒 还没有结束 要中断 停止t线程
        Thread.sleep(5000);
        //告诉t线程 要中断了 设置t线程的中断标志位为true 由t的代码自行决定是否中断
        //如果t线程处于阻塞状态，会抛出InterruptedException
        t.interrupt();
        System.out.println("t stop");
    }
}
