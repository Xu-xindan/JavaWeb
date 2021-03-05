package v3;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RequestResponseTask implements Runnable {
    private static final String DOC_BASE="C:\\Users\\xxd\\Desktop\\锦囊\\java\\Http项目预备阶段\\docBase";
    private final Socket socket;

    public RequestResponseTask(Socket socket) {
        this.socket = socket;
    }

    private static final Map<String,String> mimeTypeMap=new HashMap<>();
    static {
        mimeTypeMap.put("txt","txt/plain");
        mimeTypeMap.put("html", "text/html");
        mimeTypeMap.put("js", "application/javascript");
        mimeTypeMap.put("jpg","image/jpeg");
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
            scanner.nextLine(); // 读取出来 HTTP 版本信息，暂时不用，所以没保存

            String requestURI = path;
            String queryString = "";
            if (path.contains("?")) {
                int i = path.indexOf("?");
                requestURI = path.substring(0, i);
                queryString = path.substring(i + 1);
            }
            System.out.println(requestURI);

            Map<String, String> headers = new HashMap<>();
            // 通过 scanner，读取请求头
            String headerLine;
            while (scanner.hasNextLine() && !(headerLine = scanner.nextLine()).isEmpty()) {
                // 通过 ":" 分割
                String[] part = headerLine.split(":");
                String name = part[0].trim().toLowerCase(); // HTTP 的 header-name 大小写不敏感
                String value = part[1].trim();

                headers.put(name, value);
            }

            // 通过类似这样的处理，使得 / => /index.html 同样的效果
            if (requestURI.equals("/")) {
                // welcome-file
                requestURI = "/index.html";
            }

            if (requestURI.equals("/set-cookie")) {
                OutputStream outputStream = socket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
                PrintWriter printWriter = new PrintWriter(writer);

                printWriter.printf("HTTP/1.0 307 Temporary Redirect\r\n");
                printWriter.printf("Set-Cookie: username=peixinchen\r\n");
                printWriter.printf("Location: profile\r\n");
                printWriter.printf("\r\n");
                printWriter.flush();
            } else if (requestURI.equals("/profile")) {
                OutputStream outputStream = socket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
                PrintWriter printWriter = new PrintWriter(writer);

                String username = null;
                // 从 cookie 中获取 username
                String cookie = headers.getOrDefault("cookie", "");
                System.out.println("Cookie value:" + cookie);
                for(String cookieKV:cookie.split(";")){
                    String[] kv=cookieKV.split("=");
                    String cookieName=kv[0];
                    String cookieValue=kv[1];
                    if()
                }

                printWriter.printf("HTTP/1.0 200 OK\r\n");
                printWriter.printf("Content-Type: text/html; charset=utf-8\r\n");
                printWriter.printf("\r\n");
                if (username != null) {
                    printWriter.printf("<h1>当前用户是: %s</h1>", username);
                } else {
                    printWriter.printf("<h1>您需要先进行登录</h1>");
                }
                printWriter.flush();
            } else if (requestURI.equals("/redirect-to")) {
                OutputStream outputStream = socket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
                PrintWriter printWriter = new PrintWriter(writer);

                printWriter.printf("HTTP/1.0 307 Temporary Redirect\r\n");
                printWriter.printf("Location: /hello.jpg\r\n");
                printWriter.printf("\r\n");
                printWriter.flush();

            } else if (requestURI.equals("/goodbye.html")) {
                OutputStream outputStream = socket.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
                PrintWriter printWriter = new PrintWriter(writer);


                String target = "世界";
                for (String kv : queryString.split("&")) {
                    if (kv.isEmpty()) {
                        continue;
                    }
                    String[] part = kv.split("=");

                    String key = URLDecoder.decode(part[0], "UTF-8");
                    String value = URLDecoder.decode(part[1], "UTF-8");
                    // URLEncoder.encode() 这个是进行 URL 编码

                    if (key.equals("target")) {
                        target = value;
                    }
                }

                printWriter.printf("HTTP/1.0 200 OK\r\n");
                printWriter.printf("Content-Type: text/html; charset=utf-8\r\n");
                printWriter.printf("\r\n");
                printWriter.printf("<h1>再见 %s</h1>", target);
                printWriter.flush();
            } else {
                String filePath = DOC_BASE + requestURI;  // 用户请求的静态资源对应的路径
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
