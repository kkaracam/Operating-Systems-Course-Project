public class Memory {

	private int memorySize;
	private char[] memory;
	private int [] bitmap;

	public Memory(int size) {
		memorySize = size;
		memory = new char[size];
		bitmap = new int[size];

		for (int i = 0; i < size; i++)
			bitmap[i] = 0;
	}

	void addInstructions(char[] buffer, int bufferSize, int BR)
	{
			for (int i = BR; i < bufferSize+BR; i++)
			{
				this.memory[i] = buffer[i - BR];
				this.bitmap[i] = 1;
			}

	}

	public int firstFit(ProcessImage process){
		boolean found = false;
		int i;
			for (i = 0; i < memorySize; i++){
				int counter = 0;
				while ( i < memorySize && bitmap[i] == 0){
					if (counter == process.LR){
						found = true;
						break;
					}
					counter++;
					i++;
				}
				if (found)
					break;
			}
		return found ? i : -1;
	}

	public void removeProcess(ProcessImage process){
		for (int i = process.BR; i <= process.LR; i++)
		{
			this.bitmap[i] = 0;
		}
	}

	char[]getInstruction(int PC, int BR)
	{
		char[]instruction = new char[4];
		//System.out.print("Access memory location: " + (PC + BR) + "\n");
		instruction[0]=memory[PC+BR];
		instruction[1]=memory[PC+BR+1];
		instruction[2]=memory[PC+BR+2];
		instruction[3]=memory[PC+BR+3];

		return instruction;

	}


	public int getMemorySize() {
		return memorySize;
	}

}
