import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileInputProducer extends Thread {
    private Memory memory;
    private FileInputStorage storage;

    private volatile boolean isRunning;

    public FileInputProducer(Memory memo, FileInputStorage stor) {
        this.memory = memo;
        this.storage = stor;
    }

    @Override
    public void run(){
        isRunning = true;
        Assembler as = new Assembler();

        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("inputSequence.txt"), StandardCharsets.UTF_8));
            //OutputStreamWriter wr = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);

            String line="";

            while((line = br.readLine()) != null && line.trim().isEmpty()==false) {
                String list[]=line.split(" ");
                String outName = list[0].substring(0, list[0].indexOf(".")) + ".bin";
                int size = as.createBinaryFile(list[0], outName);   // Create "*.bin file"

                ProcessImage process = new ProcessImage(outName, 0, size);
                //System.out.print("Size : " + size + "\n");
                storage.insertItem(process);
                sleep(Integer.parseInt(list[1]));
            }

            br.close();
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
