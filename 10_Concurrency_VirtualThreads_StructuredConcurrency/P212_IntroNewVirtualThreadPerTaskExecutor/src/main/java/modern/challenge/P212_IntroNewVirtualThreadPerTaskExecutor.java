package modern.challenge;

import java.util.concurrent.*;
import java.util.logging.Logger;
 
public class P212_IntroNewVirtualThreadPerTaskExecutor {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int NUMBER_OF_TASKS = 15;

    static class SimpleThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            //return new Thread(r);                     // classic thread
            return Thread.ofVirtual().name("virtualThread", 0).unstarted(r); // virtual thread
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] [%4$-7s] %5$s %n");
        Runnable taskr = () -> logger.info(Thread.currentThread().toString());
        Callable<Boolean> taskc = () -> {
            logger.info("callable " + Thread.currentThread().toString());
            return true;
        };

        logger.info("Runnable:");
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                logger.info("Runnable:" + i);
                executor.submit(taskr);
                //Future<?> future = executor.submit(taskr);
            }
        }

        logger.info("Callable:");
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                logger.info("Callable:" + i);
                executor.submit(taskc);
                // taskc.call();  //platform thread
                //Future<Boolean> future = executor.submit(taskc);
            }
        }
    }
}