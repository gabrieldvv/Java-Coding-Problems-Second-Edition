package modern.challenge;

import java.time.Duration;
import java.util.logging.Logger;

// THIS APPLICATION WILL JUST HANG ON, SO YOU SHOULD STOP IT MANUALLY

public class P215_ContextSwitching3 {

    private static final Logger logger = Logger.getLogger(P215_ContextSwitching3.class.getName());
    
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("java.util.logging.SimpleFormatter.format",  "[%1$tT] [%4$-7s] %5$s %n");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");

        Runnable slowTask = () -> {
            logger.info(() -> Thread.currentThread().toString() + " | working on something long");
            logger.info(() -> Thread.currentThread().toString() + " | break time (non-blocking)");
            while(dummyTrue()) {} // non-blocking            
            logger.info(() -> Thread.currentThread().toString() + " | work done");
        };
        
        Runnable fastTask = () -> {
            logger.info(() -> Thread.currentThread().toString() + " | working on something");            
            logger.info(() -> Thread.currentThread().toString() + " | break time (blocking)");
            try { Thread.sleep(Duration.ofSeconds(1)); } catch (InterruptedException ex) {} // blocking
            logger.info(() -> Thread.currentThread().toString() + " | work done");
        };

        Thread ft = Thread.ofVirtual().name("fast-", 0).start(fastTask);
        Thread st = Thread.ofVirtual().name("slow-", 0).start(slowTask);

        st.join(); // interrupt after 5 seconds,   st.join(Duration.ofSeconds(5));
        ft.join();
    }
    
    static boolean dummyTrue() { return true; }
}