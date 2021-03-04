package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        //作为TCP的被动连接方，需要监听一个固定的端口（选择8080）
        //端口是随意选择的，只要该端口目前没有被占用

        //下面语句完成后，TCP端口就完成了LISTEN
        ServerSocket serverSocket=new ServerSocket(8080);
        //Thread.sleep(10000000);
        while (true) {
            //三次握手发生在accept的调用过程
            //我们通过socket向os要已经建立的TCP连接
            //1.目前有已经建立好的连接，os立即返回
            //2.否则，会一直等，直到有client过来建立好连接
            Socket socket = serverSocket.accept();
            //socket代表已经连接好的一条TCP连接（三次握手结束）
            System.out.println("有一条TCP连接已建立");
            //socket关联的TCP连接已经建立(ESTABLISHED)
            //
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            String line = scanner.nextLine(); //先读取客户端发来的消息
            System.out.println(line);

            //我们可以通过将数据写入outStream，经过os内部的TCP机制，将数据发送给client
            OutputStream outputStream = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.printf("今天的项目很不错\r\n");
            printWriter.flush();
            socket.close();
            //四次挥手结束
        }
    }
}
