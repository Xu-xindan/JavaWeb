package lesson1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SingleThread {
    public static void main(String[] args) {
        //耗时多的任务
        calculate(new ArrayList<>());
        calculate(new ArrayList<>());
        //阻塞任务
        Scanner sc=new Scanner(System.in);
        print(sc);
        print(sc);
    }
    public static int calculate(List<Integer> list){
        //计算量非常大
        return 0;
    }

    public static void print(Scanner sc){
        while(sc.hasNext()){
            System.out.println(sc.nextLine());
        }
    }
}
