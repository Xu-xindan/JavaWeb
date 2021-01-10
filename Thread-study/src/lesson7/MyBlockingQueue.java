package lesson7;

/*
* 实现阻塞队列
* 1、线程安全问题：在多线程下
* */
public class MyBlockingQueue<T>{

    //使用数组使用循环队列
    private Object[] queue;
    //存放元素的索引
    private int putIndex;
    //取元素的索引
    private int takeIndex;
    //当前存放元素的数量
    private int size;

    public MyBlockingQueue(int len){
        queue=new Object[len];
    }

    //存放元素 考虑1、putIndex超过数组长度 2、size达到数组最大长度
    public synchronized void put(T e) throws InterruptedException{
        while (size==queue.length){
            this.wait();
        }
        //存放到数组中放元素的位置
        queue[putIndex]=e;
        //存放位置超过数组的最大索引，需要取模放在0位置
        putIndex=(putIndex+1)%queue.length;
        size++;
        notifyAll();
    }

    //取元素
    public synchronized T take() throws InterruptedException {
        while (size==0){
            wait();
        }
        T t= (T) queue[takeIndex];
        queue[takeIndex]=null;
        takeIndex=(takeIndex+1)%queue.length;
        size--;
        notifyAll();
        return t;
    }
    public synchronized int size(){
        return size;
    }

    public static void main(String[] args) throws InterruptedException {
        MyBlockingQueue<Integer> queue=new MyBlockingQueue<>(10);
        //多线程的调试工具：1、写打印语句 2、jconsole 3、debug在有些场景不一定适用
        for(int i=0;i<3;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for(int j=0;j<1000;j++){
                            queue.put(j);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
