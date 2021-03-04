package socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("182.254.132.183",8080);
        //socket已经建立好了
        InputStream inputStream=socket.getInputStream();
        Scanner scanner=new Scanner(inputStream,"UTF-8");

        OutputStream outputStream=socket.getOutputStream();
        Writer writer=new OutputStreamWriter(outputStream,"UTF-8");
        PrintWriter printWriter=new PrintWriter(writer);
        printWriter.printf("累了\r\n");//向服务器发送消息
        printWriter.flush();//只要进行了刷新，才能把数据真正写入
        String message=scanner.nextLine();
        System.out.println(message);
        socket.close();
    }
}
