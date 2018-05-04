import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ConsoleInputStorage {

    private int MAX_STORAGE = 5;
    private int itemSize = 0;
    private volatile List<Integer> consoleInputQueue;
    private Semaphore mutex = new Semaphore(1);
    private Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(MAX_STORAGE);

    public ConsoleInputStorage (){
        MAX_STORAGE = 5;
        itemSize = 0;
        consoleInputQueue =  new ArrayList<Integer>();
    }

    public int removeItem() {
        int tweet = -1;
        try {
            full.acquire();
            mutex.acquire();

            tweet = removeFromBuffer();
            mutex.release();
            empty.release();
        } catch (InterruptedException e) {
            mutex.release();
            full.release();
        }
        return tweet;
    }

    public void insertItem(int item) {
        try {
            empty.acquire();
            mutex.acquire();

            insertToBuffer(item);

            mutex.release();
            full.release();
        } catch (InterruptedException e) {
            mutex.release();
            empty.release();
        }
    }

    private void insertToBuffer(int item)
    {
        consoleInputQueue.add(item);
        itemSize++;
        //System.out.println(item + " is added to storage. Storage size: " + itemSize);
    }

    private int removeFromBuffer()
    {
        int in = consoleInputQueue.remove(0);
        itemSize--;
        //System.out.println(in + " is removed from storage. Storage size: " + itemSize);
        return in;
    }
}
