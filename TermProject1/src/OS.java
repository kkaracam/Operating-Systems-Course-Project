import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class OS extends Thread {

	private final int QUANTUM = 5;

	private CPU cpu;
	private Memory memory;
	private List<ProcessImage> readyQueue;
	private List<ProcessImage> blockedQueue;
	private Semaphore mutex;	
	private InputThread inputThread;

	public OS(int size) {
		this.memory = new Memory(size);
		this.cpu = new CPU(memory);
		this.mutex=new Semaphore(1);
		this.readyQueue = new ArrayList<ProcessImage>();
		this.blockedQueue = new ArrayList<ProcessImage>();

		this.inputThread = new InputThread(mutex, blockedQueue, readyQueue);
		inputThread.start();
	}

	@Override
	public void run() {
		//TODO implement OS Logic

		int counter = 0;
		ProcessImage current_process = readyQueue.remove(0);
		boolean cont_switch = true;

		while(true){
			//System.out.print("\nInside OS loop.");
			//System.out.print("\n" + current_process.PC);
			//System.out.print("\n" + readyQueue.isEmpty());
			//System.out.print("\n" + blockedQueue.isEmpty());
			if (/*readyQueue.isEmpty() && blockedQueue.isEmpty() && */current_process.PC >= memory.size()){
				System.out.print("\nBreak loop when " + current_process.PC + " " + current_process.LR);
				break;
			}
			try {
				//System.out.print("\n" + blockedQueue.isEmpty());

				mutex.acquire();
				//System.out.print("\n" + current_process.processName);
				if (!readyQueue.isEmpty() && cont_switch){
					if (current_process.PC == 36)
						System.out.print("\nRemoved from ready gueue" + readyQueue.size());
					current_process = readyQueue.remove(0);

				}
				cpu.copyProcess(current_process);

				//System.out.print("\n" + cpu.getProcessName());
				//System.out.print("\n" + cpu.getV());
				cpu.fetch(cpu.getBR(), cpu.getPC());
				int exe_res = cpu.decodeExecute(cpu.getIR());

				//System.out.print("\n" + exe_res);
				if (exe_res == 1){
					//System.out.print("\n" + cpu.getV());
					current_process = cpu.getProcess();
					blockedQueue.add(current_process);
					//System.out.print("\n" + blockedQueue.isEmpty());
					//readyQueue.add(current_process);
					cont_switch = true;
				}
				else if (exe_res == 2){
					System.out.print("\nValue of the Register V: " + cpu.getV());
					current_process = cpu.getProcess();
					readyQueue.add(current_process);
					cont_switch = true;
				}
				else{

					current_process = cpu.getProcess();

					//System.out.print("\n" + cpu.getProcessName());

					counter++;
					if (counter == 5){
						readyQueue.add(current_process);
						cont_switch = true;
					}
					else
						cont_switch = false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			mutex.release();
			System.out.print("\n");
			//System.out.print("\n" + readyQueue.isEmpty());
			//System.out.print("\n" + (current_process.PC == current_process.LR));
		}


		//When OS thread stops stop the InputTHreadx
		inputThread.stopThread();

		try {
			FileWriter wr = new FileWriter("processRegisterDump.bin");
			System.out.print("\nWriting file...");
			wr.write(current_process.processName + ":\n");
			wr.write(System.lineSeparator());
			wr.write("S0: " + current_process.S0);
			wr.write(System.lineSeparator());
			wr.write("S1: " + current_process.S1);
			wr.write(System.lineSeparator());
			wr.write("S2: " + current_process.S2);
			wr.write(System.lineSeparator());
			wr.write("S3: " + current_process.S3);
			wr.write(System.lineSeparator());
			wr.write("S4: " + current_process.S4);
			wr.write(System.lineSeparator());
			wr.write("S5: " + current_process.S5);
			wr.write(System.lineSeparator());
			wr.write("S6: " + current_process.S6);
			wr.write(System.lineSeparator());
			wr.write("S7: " + current_process.S7);
			wr.write(System.lineSeparator());
			wr.write("$0: " + current_process.$0);
			wr.write(System.lineSeparator());
			wr.write("PC: " + current_process.PC);
			wr.write(System.lineSeparator());
			wr.write("V: " + current_process.V);
			wr.write(System.lineSeparator());
			wr.write("IR: " + current_process.IR);
			wr.write(System.lineSeparator());
			wr.write("BR: " + current_process.BR);
			wr.write(System.lineSeparator());
			wr.write("LR: " + current_process.LR);
			wr.write(System.lineSeparator());

			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("\nOS is terminated...");
	}

	public void enqueue(ProcessImage p){
		readyQueue.add(p);
	}
	public ProcessImage dequeue(){
		return readyQueue.remove(0);
	}

	public int loadMemory(char[] p){
		return memory.readIntoMemory(p);
	}
}
