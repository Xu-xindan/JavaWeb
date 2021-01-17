package lesson7;

import java.util.TimerTask;
import java.util.concurrent.*;

public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        ThreadPoolExecutor pool=new ThreadPoolExecutor(
                5,//核心线程数--》正式员工
                10,//最大线程数--》正式员工+临时员工
                60,
                TimeUnit.SECONDS,//idle线程的空闲时间：临时员工的最大存活时间，超过时间就解雇
                new LinkedBlockingDeque<>(),//阻塞队列：任务存放的地方
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return newThread(r);
                    }
                },//创建线程的工厂类：线程池创建线程时，调用该工厂的方法创建线程--》招聘员工的标准
                new ThreadPoolExecutor.AbortPolicy()//拒绝策略：达到最大线程数且阻塞队列已满，采取拒绝
                /*
                * 策略：AbortPolicy:直接抛RejectedException(不提供handler时的默认策略)
                * CallerRunsPolicy:谁（某个线程）交给我（线程池）任务，我拒绝执行，由谁自己执行
                * DiscardPolicy:交给我的任务直接丢弃
                * DiscardOldPolicy;丢弃阻塞队列中最旧的任务
                * */
        );
        for(int i=0;i<20;i++){

        }
        //线程池有4个快捷创建方式（实际工作不使用）
        //实际工作使用ThreadPoolExecutor
        ExecutorService pool1=Executors.newSingleThreadExecutor();//单线程池
        ExecutorService pool2=Executors.newCachedThreadPool();//缓存的线程池
        ExecutorService pool3=Executors.newScheduledThreadPool(4);//计划任务线程池
        ScheduledExecutorService pool4=Executors.newScheduledThreadPool(4);
        pool4.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        },2,TimeUnit.SECONDS);//延迟2秒
        pool4.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        },2,1,TimeUnit.SECONDS);//间隔一秒执行一次
    }
}
