import java.util.List;
import java.util.concurrent.Semaphore;

public class ConsoleInputConsumer extends Thread {
    private Memory memory;
    private ConsoleInputStorage storage;
    private volatile List<ProcessImage> readyQueue;
    private volatile List<ProcessImage> blockedQueue;
    private Semaphore mutex;

    private volatile boolean isRunning;

    public ConsoleInputConsumer(Memory memo, ConsoleInputStorage stor, List<ProcessImage> readyQ, List<ProcessImage> blockedQ, Semaphore mtx) {
        this.memory = memo;
        this.storage = stor;
        this.readyQueue = readyQ;
        this.blockedQueue = blockedQ;
        this.mutex = mtx;
    }

    @Override
    public void run(){
        try {
            isRunning = true;
            while(isRunning) {
                if (!blockedQueue.isEmpty()){
                    int val = storage.removeItem();


                    mutex.acquire();
                    ProcessImage process = blockedQueue.remove(0);
                    System.out.print(process.processName + " is removed from Blocked Queue.\n");
                    mutex.release();

                    process.V = val;

                    mutex.acquire();
                    readyQueue.add(process);
                    System.out.print(process.processName + " is added to Ready Queue.\n");
                    mutex.release();
                }
                else
                    sleep(2000);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
