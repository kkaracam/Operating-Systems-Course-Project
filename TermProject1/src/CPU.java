
public class CPU {

	private Memory memory;
	private String processName;
	private int S0;
	private int S1;
	private int S2;
	private int S3;
	private int S4;
	private int S5;
	private int S6;
	private int S7;
	private int $0;
	private int PC;
	private int V;
	private int IR;
	private int BR;
	private int LR;

	public CPU(Memory memory) {
		this.memory=memory;
	}

	public void copyProcess(ProcessImage p){
		processName = p.processName;
		S0 = p.S0;
		S1 = p.S1;
		S2 = p.S2;
		S3 = p.S3;
		S4 = p.S4;
		S5 = p.S5;
		S6 = p.S6;
		S7 = p.S7;
		$0 = p.$0;
		PC = p.PC;
		V = p.V;
		IR = p.IR;
		BR = p.BR;
		LR = p.LR;
	}

	public ProcessImage getProcess(){
		ProcessImage p = new ProcessImage(getProcessName());
		p.S0 = getS0();
		p.S1 = getS1();
		p.S2 = getS2();
		p.S3 = getS3();
		p.S4 = getS4();
		p.S5 = getS5();
		p.S6 = getS6();
		p.S7 = getS7();
		p.$0 =get$0();
		p.PC = getPC();
		p.V = getV();
		p.IR = getIR();
		p.BR = getBR();
		p.LR = getLR();

		return p;
	}
	private int getRegister(String s){
		if (s.equals("00000")){return PC;}
		else if (s.equals("00010")){return V;}
		else if (s.equals("00011")){return S0;}
		else if (s.equals("00100")){return S1;}
		else if (s.equals("00101")){return S2;}
		else if (s.equals("00110")){return S3;}
		else if (s.equals("00111")){return S4;}
		else if (s.equals("01000")){return S5;}
		else if (s.equals("01001")){return S6;}
		else if (s.equals("01010")){return S7;}
		else if (s.equals("01011")){return BR;}
		else if (s.equals("01100")){return $0;}
		else{return -1;}
	}

	private void setRegister(String s, int val){
		if (s.equals("00000")){PC = val;}
		else if (s.equals("00010")){V = val;}
		else if (s.equals("00011")){S0 = val;}
		else if (s.equals("00100")){S1 = val;}
		else if (s.equals("00101")){S2 = val;}
		else if (s.equals("00110")){S3 = val;}
		else if (s.equals("00111")){S4 = val;}
		else if (s.equals("01000")){S5 = val;}
		else if (s.equals("01001")){S6 = val;}
		else if (s.equals("01010")){S7 = val;}
		else if (s.equals("01011")){BR = val;}
		else if (s.equals("01100")){$0 = val;}
		//System.out.print("\nInside setRegister...");
	}

	public int fetch(int br, int pc){
		String s = "", res = "";

		//System.out.print("\n" + br + " " + BR + " " + pc + " " + PC + " " +LR);

		for(int i = 0; i < 4; i++){
			int asc = (int)memory.getElement(i+pc+br);
			s = Integer.toBinaryString(asc);
			while(s.length() < 8){
				s = "0" + s;
			}
			//System.out.print("\n" + s);
			res += s;
		}
		while(res.length() < 32){
			res = "0" + res;
		}
		setPC(getPC()+4);
		IR = CharSeqToInt(res,32);
		//System.out.print("\n" + IR);
		return IR;
	}

	public int decodeExecute(int ir){
		String bins = Integer.toBinaryString(ir);
		while(bins.length() < 32){
			bins = "0" + bins;
		}
		//System.out.print("\n" + bins);

		if (bins.equals("00000000000000000000000000000000")){
			System.out.print("\nInside SYSCALL");
			if (V == 0){
				return 1;
			}
			else{
				return 2;
			}
		}
		else{
			String ins = "";
			int i = 0;

			//System.out.print("\n" + bins);

			while(i < 32){
				ins += bins.charAt(i);
				i++;
				if (ins.length() < 6){
					for (int x = ins.length(); x < 6; x++){
						ins += bins.charAt(i);
						i++;
					}
				}
				//System.out.print("\n" + ins);
				//System.out.print("\n" + ins.substring(ins.length() - 6, ins.length()));
				if(ins.substring(ins.length() - 6, ins.length()).equals("010000") && i == 6){
					// ADD operation
					String sreg1 = "", sreg2 = "", sreg3 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg3 += bins.charAt(i);
						i++;
					}
					int reg1 = getRegister(sreg1);
					int reg2 = getRegister(sreg2);
					int reg3 = getRegister(sreg3);

					System.out.print("\nInside ADD");
					System.out.print("\n" + reg1);
					reg1 = reg2 + reg3;
					System.out.print("\n" + reg1);
					setRegister(sreg1, reg1);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("010001") && i == 6){
					// SUB operation
					String sreg1 = "", sreg2 = "", sreg3 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg3 += bins.charAt(i);
						i++;
					}
					int reg1 = getRegister(sreg1);
					int reg2 = getRegister(sreg2);
					int reg3 = getRegister(sreg3);

					System.out.print("\nInside SUB");
					System.out.print("\n" + reg1);
					reg1 = reg2 - reg3;
					System.out.print("\n" + reg1);
					setRegister(sreg1, reg1);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("000010") && i == 6){
					// ADDI operation
					String sreg1 = "", sreg2 = "", sreg3 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					sreg3 = bins.substring(i, bins.length());
					int reg1 = getRegister(sreg1);
					int reg2 = getRegister(sreg2);
					int reg3 = CharSeqToInt(sreg3,16);

					System.out.print("\nInside ADDI");
					System.out.print("\n" + reg1);
					reg1 = reg2 + reg3;
					System.out.print("\n" + reg1);
					setRegister(sreg1, reg1);
					//System.out.print("\n" + reg1);
					//System.out.print("\n" + sreg1);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("000011") && i == 6){
					//SUBI operation
					String sreg1 = "", sreg2 = "", sreg3 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					sreg3 = bins.substring(i, bins.length());
					int reg1;
					int reg2 = getRegister(sreg2);
					int reg3 = CharSeqToInt(sreg3,16);

					System.out.print("\nInside SUBI");
					reg1 = reg2 - reg3;
					setRegister(sreg1, reg1);
					//System.out.print("\n" + reg1);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("011111") && i == 6){
					// ORI operation
					String sreg1 = "", sreg2 = "", sreg3 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					sreg3 = bins.substring(i, bins.length());
					int reg1;
					int reg2 = getRegister(sreg2);
					int reg3 = CharSeqToInt(sreg3,16);

					System.out.print("\nInside ORI");
					System.out.print("\n" + reg2);
					reg1 = reg2 | reg3;
					System.out.print("\n" + reg1);
					setRegister(sreg1, reg1);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("000111") && i == 6){
					// BEQ operation

					System.out.print("\nInside BEQ");

					String sreg1 = "", sreg2 = "", sreg3 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					sreg3 = bins.substring(i, bins.length());
					System.out.print("\n" + sreg3);
					int reg1 = getRegister(sreg1);
					int reg2 = getRegister(sreg2);
					int reg3 = CharSeqToInt(sreg3,16);

					System.out.print("\n" + reg1 + " " + reg2 + " " + reg3);
					System.out.print("\n" + PC);
					if (reg1 == reg2){
						PC = reg3;
					}
					System.out.print("\n" + PC);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("001000") && i == 6){
					// LUI operation
					String sreg1 = "", sreg2 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					sreg2 = bins.substring(i, bins.length());
					int reg1;
					int reg2 = CharSeqToInt(sreg2,sreg2.length());
					//System.out.print("\n" + sreg1);
					//System.out.print("\n" + reg2);
					System.out.print("\nInside LUI");

					reg1 = reg2 << 16;
					setRegister(sreg1, reg1);
					//System.out.print("\n" + reg1);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("001001") && i == 6){
					// LW operation
					String sreg1 = "", sreg2 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					int reg1;
					int reg2 = getRegister(sreg2);

					reg1 = memory.getElement(BR + reg2);
					setRegister(sreg1, reg1);
					break;
				}
				else if (ins.substring(ins.length() - 6, ins.length()).equals("001010") && i == 6){
					// SW operation
					String sreg1 = "", sreg2 = "";
					for (int j = 0; j < 5; j++){
						sreg1 += bins.charAt(i);
						i++;
					}
					for (int j = 0; j < 5; j++){
						sreg2 += bins.charAt(i);
						i++;
					}
					int reg1 = getRegister(sreg1);
					int reg2 = getRegister(sreg2);

					memory.add((char)reg1, BR+reg2);
					break;
				}
			}
			return 0;
		}
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public int getS0() {
		return S0;
	}

	public void setS0(int s0) {
		S0 = s0;
	}

	public int getS1() {
		return S1;
	}

	public void setS1(int s1) {	S1 = s1;}

	public int getS2() {
		return S2;
	}

	public void setS2(int s2) {
		S2 = s2;
	}

	public int getS3() {
		return S3;
	}

	public void setS3(int s3) {
		S3 = s3;
	}

	public int getS4() {
		return S4;
	}

	public void setS4(int s4) {
		S4 = s4;
	}

	public int getS5() {
		return S5;
	}

	public void setS5(int s5) {
		S5 = s5;
	}

	public int getS6() {
		return S6;
	}

	public void setS6(int s6) {
		S6 = s6;
	}

	public int getS7() {
		return S7;
	}

	public void setS7(int s7) {
		S7 = s7;
	}

	public int get$0() {
		return $0;
	}

	public void set$0(int $0) {
		this.$0 = $0;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public int getV() {
		return V;
	}

	public void setV(int v) {
		V = v;
	}

	public int getIR() {
		return IR;
	}

	public void setIR(int iR) {
		IR = iR;
	}

	public int getBR() {
		return BR;
	}

	public void setBR(int bR) {
		BR = bR;
	}

	public int getLR() {
		return LR;
	}

	public void setLR(int lR) {
		LR = lR;
	}

/*	private String CharToBin(String S) {
		String res = "";
		for (int i = 0; i < 4; i++) {
			int tot = (int) S.charAt(i);
			//System.out.print("\n" + tot + " " + S.charAt(i));
			for (int j = 0; j < 8; j++){
				if (tot / Math.pow(2,7-j) > 0){
					res += '1';
				}
				else{
					res += '0';
				}
				tot = tot % (int)Math.pow(2,7-j);
			}
			System.out.print("\n" + res);
		}
		System.out.print("\n" + res);
		return res;
	}*/

	private String IntToBin(int ir) {
		String ins = "";
		int tot = ir;
		for (int i = 0; i < 32; i++) {
			if (tot / Math.pow(2,32-i) > 0){
				ins += '1';
			}
			else{
				ins += '0';
			}
			tot = tot % (int)Math.pow(2,32-i);
		}
		return ins;
	}

	private int CharSeqToInt(String S, int lmt) {
		int total = 0;
		//System.out.print("\n" + S);
		for (int i = lmt-1; i >= 0; i--) {
			if (S.charAt(lmt-1-i)=='1')
				total += (int)Math.pow(2, i);
		}
		//System.out.print("\n" + total);
		return total;
	}
}
