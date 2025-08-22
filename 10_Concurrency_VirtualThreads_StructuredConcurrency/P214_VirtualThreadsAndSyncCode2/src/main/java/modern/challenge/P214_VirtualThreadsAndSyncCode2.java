package modern.challenge;

import java.time.Duration;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

public class P214_VirtualThreadsAndSyncCode2 {

    private static final Logger logger = Logger.getLogger(P214_VirtualThreadsAndSyncCode2.class.getName());

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [%4$-7s] %5$s %n");

        Runnable offer = () -> {
            logger.info(() -> Thread.currentThread() + " sleeps for 5 seconds");
            try { Thread.sleep(Duration.ofSeconds(5)); } catch (InterruptedException ex) {}
            logger.info(() -> Thread.currentThread() + " inserts in the queue");
            queue.add(1234);
        };
        
        Runnable take = () -> {
            logger.info(() -> Thread.currentThread() + " can't take from the queue yet");
            try {
                int maxint = queue.take();
                logger.info(() -> Thread.currentThread() + " took from queue: " + maxint);
            } catch (InterruptedException ex) {} 
        };

        logger.info("Before running the task ...");
        Thread offerThread = Thread.ofPlatform().name("offerThread").start(offer);
        logger.info(offerThread.toString());
        Thread takeThread = Thread.ofVirtual().name("takeThread").start(take);
        logger.info(takeThread.toString());
        takeThread.join();
        
        logger.info("After running the task ...");
    }
}