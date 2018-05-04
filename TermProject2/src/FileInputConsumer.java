import java.util.List;
import java.util.concurrent.Semaphore;

public class FileInputConsumer extends Thread {
    private Memory memory;
    private FileInputStorage storage;
    private volatile List<ProcessImage> readyQueue;
    private Semaphore mutex;

    private volatile boolean isRunning;

    public FileInputConsumer(Memory memo, FileInputStorage stor, List<ProcessImage> readyQ, Semaphore mtx) {
        this.memory = memo;
        this.storage = stor;
        this.readyQueue = readyQ;
        this.mutex = mtx;
    }

    @Override
    public void run(){
        Assembler as = new Assembler();
        isRunning = true;
        try {
            while(isRunning) {
                ProcessImage process = storage.removeItem();
                //System.out.print(process.processName + " ERROR !!!!!\n");
                int base = memory.firstFit(process);
                while (base == -1) {
                    sleep(2000);
                    base = memory.firstFit(process);
                }
                char[] ins;
                ins = as.readBinaryFile(process.LR, process.processName);
                process.BR = base;
                memory.addInstructions(ins, process.LR, process.BR);

                mutex.acquire();
                readyQueue.add(process);
                mutex.release();

            }
        }
        catch(Exception e) {
            e.printStackTrace();
            //System.out.print( " ERROR !!!!!\n");
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
