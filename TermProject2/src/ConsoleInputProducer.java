import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ConsoleInputProducer extends Thread {
    private Memory memory;
    private ConsoleInputStorage storage;

    private volatile boolean isRunning;

    public ConsoleInputProducer(Memory memo, ConsoleInputStorage stor) {
        this.memory = memo;
        this.storage = stor;
    }

    @Override
    public void run(){
        isRunning = true;
        int i = -1;
        try {
            Scanner in = new Scanner(System.in);
            while(isRunning) {
                i = in.nextInt();

                storage.insertItem(i);
            }
            in.close();
            System.out.println("All Processes are Produced...");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
