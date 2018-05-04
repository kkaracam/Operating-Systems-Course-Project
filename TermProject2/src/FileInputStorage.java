import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FileInputStorage {

    private int MAX_STORAGE = 5;
    private int itemSize = 0;
    private volatile List<ProcessImage> fileInputQueue;
    private Semaphore mutex = new Semaphore(1);
    private Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(MAX_STORAGE);

    public FileInputStorage (){
        MAX_STORAGE = 5;
        itemSize = 0;
        fileInputQueue =  new ArrayList<ProcessImage>();
    }

    public ProcessImage removeItem() {
        ProcessImage tweet = new ProcessImage();
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

    public void insertItem(ProcessImage item) {
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

    private void insertToBuffer(ProcessImage item)
    {
        fileInputQueue.add(item);
        itemSize++;
        //System.out.println(item.processName + " is added to storage. Storage size: " + itemSize);
    }

    private ProcessImage removeFromBuffer()
    {
        ProcessImage pro = fileInputQueue.remove(0);
        itemSize--;
        //System.out.println(pro.processName + " is removed from storage. Storage size: " + itemSize);
        return pro;
    }
}
