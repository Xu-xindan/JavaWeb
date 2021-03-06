package v3;

import java.io.*;

public class SerializableDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String filename = "D:\\课程\\2021-3-4-Java31-40班-HTTP项目\\预研阶段学习\\sessions\\users.obj";

//        User u1 = new User(1, "陈沛鑫", "男");
//        User u2 = new User(2, "林黛玉", "女");
//
//        // 把 u1 和 u2 对应的对象，序列化，并且写入到文件中
//        try (OutputStream outputStream = new FileOutputStream(filename)) {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//
//            objectOutputStream.writeObject(u1);
//            objectOutputStream.writeObject(u2);
//
//            objectOutputStream.flush();
//        }

        try (InputStream inputStream = new FileInputStream(filename)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            User u1 = (User)objectInputStream.readObject();
            User u2 = (User)objectInputStream.readObject();

            System.out.println(u1);
            System.out.println(u2);
        }
    }
}
