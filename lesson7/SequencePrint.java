package lesson7;

public class SequencePrint {
    /*
    * 有三个线程 a,b,c
    * 每个线程打印名称 让他们同时启动 输出顺序为c,b,a
    * */
    public static void main(String[] args) {
        Thread c=new Thread(()->{
            System.out.println(Thread.currentThread().getName());
        },"C");
        Thread b=new Thread(()->{
            System.out.println(Thread.currentThread().getName());
        },"B");
        Thread a=new Thread(()->{
            System.out.println(Thread.currentThread().getName());
        },"A");
    }
}
