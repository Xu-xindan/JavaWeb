package v3;

import v1.RequestResponseTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServer {
    public static void main(String[] args) throws IOException {
        // 采用固定大小线程池
        // 不是最优的方式，但简单
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket socket = serverSocket.accept();

            Runnable task = new RequestResponseTask(socket);
            threadPool.execute(task);
        }
    }
}
