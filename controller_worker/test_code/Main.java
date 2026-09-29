class SharedBox {
    private String message;
    private boolean hasMessage = false;

    // Consumer gọi hàm này để nhận tin
    public synchronized String receive() throws InterruptedException {
        while (!hasMessage) {
            System.out.println("Consumer: chưa có tin, đang chờ...");
            wait(); // nhả lock và ngủ, chờ notify()
        }
        hasMessage = false;
        notify(); // báo cho Producer biết đã nhận xong
        return message;
    }

    // Producer gọi hàm này để gửi tin
    public synchronized void send(String msg) throws InterruptedException {
        while (hasMessage) {
            wait(); // chờ Consumer nhận tin cũ trước
        }
        this.message = msg;
        hasMessage = true;
        System.out.println("Producer: đã gửi -> " + msg);
        notify(); // đánh thức Consumer
    }
}

public class Main {
    public static void main(String[] args) {
        SharedBox box = new SharedBox();

        // Producer thread
        Thread producer = new Thread(() -> {
            String[] messages = {"Xin chào", "Hôm nay đẹp trời", "Tạm biệt"};
            for (String msg : messages) {
                try {
                    box.send(msg);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Consumer thread
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    String msg = box.receive();
                    System.out.println("Consumer: nhận được -> " + msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        consumer.start();
        producer.start();
    }
}