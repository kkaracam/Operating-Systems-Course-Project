import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class InputThread extends Thread {

	private List<ProcessImage> blockedQueue;
	private List<ProcessImage> readyQueue;
	private Semaphore mutex;

	private boolean isRunning;

	public InputThread(Semaphore mtx, List<ProcessImage> blockedQ, List<ProcessImage> readyQ) {
		this.mutex = mtx;
		this.blockedQueue = blockedQ;
		this.readyQueue = readyQ;
	}

	@Override
	public void run(){
		isRunning = true;
		while (isRunning) {
			//TODO implement Input logic
			//System.out.print("\n" + mutex);
			//System.out.print("\n" + blockedQueue.isEmpty());
			try {
				mutex.acquire();
				if (!blockedQueue.isEmpty()){

					System.out.print("\nWaiting for input...\n");
					ProcessImage pr = blockedQueue.remove(0);
					Scanner reader = new Scanner(System.in);
					int res = reader.nextInt();
					pr.V = res;
					//System.out.print("\n" + pr.V + " " + res);
					readyQueue.add(pr);
				}
				mutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void stopThread() {
		System.out.print("\nInput Thread terminated...");
		isRunning = false;
	}
}
