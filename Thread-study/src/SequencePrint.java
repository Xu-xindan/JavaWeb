public class SequencePrint {
    public static void main(String[] args) {
        Thread a=new Thread(new PrintTask());
    }

    private static class PrintTask implements Runnable{

        @Override
        public void run() {

        }
    }
}
