package v2;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RequestResponseTask implements Runnable {
    // TODO: 自己运行代码时，需要修改成自己的绝对路径
    private static final String DOC_BASE = "C:\\Users\\xxd\\Desktop\\锦囊\\java\\Http项目预备阶段\\docBase";
    private final Socket socket;

    public RequestResponseTask(Socket socket) {
        this.socket = socket;
    }

    // Map<suffix, contentType>
    private static final Map<String, String> mimeTypeMap = new HashMap<>();
    static {
        mimeTypeMap.put("txt", "text/plain");
        mimeTypeMap.put("html", "text/html");
        mimeTypeMap.put("js", "application/javascript");
        mimeTypeMap.put("jpg", "image/jpeg");
    }

    @Override
    public void run() {
        try {
            System.out.println("一条 TCP 连接已经建立");

            // 进行 HTTP 请求解析 -> 解析出路径
            InputStream inputStream = socket.getInputStream();
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            scanner.next(); // 读取出来的是方法，暂时不用，所以没保存
            String path = scanner.next();
            System.out.println(path);

            // 通过类似这样的处理，使得 / => /index.html 同样的效果
            if (path.equals("/")) {
                // welcome-file
                path = "/index.html";
            }

            String filePath = DOC_BASE + path;  // 用户请求的静态资源对应的路径
            // 0. 暂时先不考虑，文件是一个目录的情况
            // 1. 判断该文件是否存在 —— File
            File resource = new File(filePath);
            System.out.println(resource);
            if (resource.exists()) {
                // 读取文件内容，并写入 response body 中

                OutputStream outputStream = socket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
                PrintWriter printWriter = new PrintWriter(writer);

                String contentType = "text/plain";
                // 找到路径对应的后缀（字符串处理）
                if (path.contains(".")) {
                    int i = path.lastIndexOf(".");
                    String suffix = path.substring(i + 1);
                    contentType = mimeTypeMap.getOrDefault(suffix, contentType);
                }
                // 如果 contentType 是 "text/..."，代表是文本
                // 我们都把字符集统一设定成 utf-8
                if (contentType.startsWith("text/")) {
                    contentType = contentType + "; charset=utf-8";
                }

                printWriter.printf("HTTP/1.0 200 OK\r\n");
                printWriter.printf("Content-Type: %s\r\n", contentType);
                printWriter.printf("\r\n");
                printWriter.flush();    // NOTICE: 千万不要忘记

                // 写入 response body
                try (InputStream resourceInputStream = new FileInputStream(resource)) {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = resourceInputStream.read(buffer);
                        if (len == -1) {
                            break;
                        }

                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.flush();
                }
            } else {
                // response 404

                OutputStream outputStream = socket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
                PrintWriter printWriter = new PrintWriter(writer);

                printWriter.printf("HTTP/1.0 404 Not Found\r\n");
                printWriter.printf("Content-Type: text/html; charset=utf-8\r\n");
                printWriter.printf("\r\n");
                printWriter.printf("<h1>%s: 对应的资源不存在</h1>", path);
                printWriter.flush();
            }
        } catch (IOException exc) {
            exc.printStackTrace(System.out);
        } finally {
            try {
                socket.close();
                System.out.println("一条 TCP 连接已经释放");
            } catch (IOException exc) {
                exc.printStackTrace(System.out);
            }
        }
    }
}
