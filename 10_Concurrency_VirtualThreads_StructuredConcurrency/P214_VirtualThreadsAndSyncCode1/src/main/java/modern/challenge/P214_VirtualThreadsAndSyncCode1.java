package modern.challenge;

import java.time.Duration;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

public class P214_VirtualThreadsAndSyncCode1 {

    private static final Logger logger = Logger.getLogger(P214_VirtualThreadsAndSyncCode1.class.getName());

    public static void main(String[] args) throws InterruptedException {

        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [%4$-7s] %5$s %n");

        Runnable offer = () -> {
            logger.info(() -> Thread.currentThread() + " sleeps for 5 seconds");
            try { Thread.sleep(Duration.ofSeconds(5)); } catch (InterruptedException ex) {}
            logger.info(() -> Thread.currentThread() + " inserts in the queue");
            queue.offer(1234);
        };

        logger.info("Before running the task ...");
        // Thread.startVirtualThread(offer);
        Thread offerThread = Thread.ofVirtual().name("offerThread").start(offer);
        logger.info(offerThread.toString());
        logger.info(() -> Thread.currentThread() + " can't take from the queue yet");
        
        int maxint = queue.take();

        logger.info(() -> Thread.currentThread() + "took from queue: " + maxint);
        logger.info(offerThread.toString());
        logger.info("After running the task ...");
    }
}


