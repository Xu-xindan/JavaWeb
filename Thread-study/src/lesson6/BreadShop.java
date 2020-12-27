package lesson6;
/*
* 面包店
* 10个生产者，每个每次生产3个  20个消费者，每个每次消费1个
* 升级版需求：面包师傅每个最多生产30次：面包店每天生产10*30*3=900个面包
*          消费者不再一直消费，把900个面包消费完结束
* */
public class BreadShop {
    private static int CONSUMER_NUM=10;//消费者数量
    private static int CONSUME_COUNT=5;//每次消费的面包数
    private static int PRODUCER_NUM=5;//生产者数量
    private static int PRODUCE_TIMES=10;//生产者生产次数
    private static int PRODUCE_COUNT=3;//每次生产的面包数
    private static int MAX_COUNT=100;

    //面包店库存
    private static int count;

    //面包店生产面包总数
    private static int PRODUCE_NUMBER;

    //消费者
    public static class Consumer implements Runnable{

        private String name;

        public Consumer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            //一直消费
            try {
                while (true) {
                    synchronized (BreadShop.class) {
                        //库存到达下限，不能继续消费，需要阻塞等待
                        if (PRODUCE_NUMBER == PRODUCER_NUM*PRODUCE_TIMES*PRODUCE_COUNT) {
                            BreadShop.class.wait();
                        } else {
                            //库存大于0 允许消费
                            System.out.printf("消费者 %s 消费了1个面包\n",name);
                            count--;
                            //代码进入阻塞线程
                            BreadShop.class.notify();
                            //模拟消费者耗时
                            Thread.sleep(500);
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //生产者
    public static class Producer implements Runnable{
        private String name;

        public Producer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                //达到生产次数
                for(int i=0;i<PRODUCE_TIMES;i++){
                    synchronized (BreadShop.class){
                        //库存达到上限，不能继续生产，需等待
                        if(count+PRODUCE_COUNT>MAX_COUNT){
                            BreadShop.class.wait();
                        }else{
                            System.out.printf("生产者 %s 生产了%s个面包\n",name,PRODUCE_COUNT);
                            count+=PRODUCE_COUNT;
                            PRODUCE_NUMBER+=PRODUCE_COUNT;
                            BreadShop.class.notifyAll();
                            Thread.sleep(500);
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //同时启动20个消费者线程
        Thread[] consumers=new Thread[20];
        for(int i=0;i<20;i++){
            consumers[i]=new Thread(new Consumer(String.valueOf(i)));
        }
        //同时启动10个生产者线程
        Thread[] producers=new Thread[10];
        for(int i=0;i<10;i++){
            producers[i]=new Thread(new Producer(String.valueOf(i)));
        }
        for(Thread t:consumers){
            t.start();
        }
        for(Thread t:producers){
            t.start();
        }
    }
}
