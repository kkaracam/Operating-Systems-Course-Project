
public class MainApplication {

	public static void main(String[] args)  {
		Assembler ass = new Assembler();

		int size = ass.createBinaryFile("assemblyInput.asm","assemblyInput.bin");
		ProcessImage process = new ProcessImage("p1");
		char[] carr = ass.readBinaryFile(size, "assemblyInput.bin");

		OS os = new OS(size);
		System.out.print("\n" + size);
		process.LR = os.loadMemory(carr);
		process.BR = process.LR - size;
		os.enqueue(process);

		os.start();

		System.out.print("\nMain is terminated");
		return;
	}

}
