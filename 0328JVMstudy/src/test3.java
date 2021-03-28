public class test3 {
    private static final int _1MB = 1024 * 1024;
    @SuppressWarnings("unused")
    public static void testAllocation() {
        byte[] allocation1,allocation2,allocation3;
        allocation1 = new byte[_1MB/4];//Eden
        // 什么时候进入老年代取决于XX:MaxTenuringThreshold的设置
        allocation2 = new byte[4 * _1MB];//Eden
        //触发 minor GC
        //GC allocation12进入老年代
        allocation3 = new byte[4 * _1MB];//Eden
        allocation3 = null;//allocation3引用之前指向的对象不可达
        allocation3 = new byte[4 * _1MB];//触发GC
    }
    public static void main(String[] args) throws Exception{
        testAllocation();
    }
}
