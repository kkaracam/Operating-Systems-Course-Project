
public class Memory {

	private int memorySize;
	private int last_index;
	private char[] memory;
	
	public Memory(int size) {
		memorySize = size;
		last_index = 0;
		memory = new char[size];
	}

	public int readIntoMemory(char[] p){
		//if (memory)
			try{
				for (; last_index < p.length; last_index++){
					memory[last_index] = p[last_index];
				}
			}
			catch (Exception e){
				System.out.println("Memory capacity is reached.");
			}
			return last_index;
	}

	public void add(char ch, int index){
		memory[index] = ch;
	}

	public int size(){
		return memorySize;
	}

	public char getElement(int i){
		return memory[i];
	}
}
