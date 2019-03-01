import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class assembly_to_bin {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("assembly.txt"));
			FileWriter writer = new FileWriter("instruction.txt");
			PrintWriter pWriter = new PrintWriter(writer);
			String line;
			while((line = br.readLine()) != null) {
				int i = 0;
				String ins = "";
				// if the char at i is alphabetic, add to ins
				// stop once the char is not alphabetic
				while((line.charAt(i) > 64 & line.charAt(i) < 90) ||
						(line.charAt(i) > 96 & line.charAt(i) < 123)) {
					ins = ins + line.charAt(i);
					i++;
					if(i >= line.length()) {
						break;
					}
				}
				String output = ins_to_bin(ins);
				
				String imm1 = "", imm2 = "";
				String rd = "", rs1 = "", rs2 = "", rs3 = "";
				if(!(ins.equals("nop"))) {
					String vals = line.substring(i+1);
					// remove instruction and split remaining part of 
					// string into registers and immediates
					String[] data = vals.split(", ");
					for(int j = 0; j < data.length; j++) {
						if(data[j].charAt(0) == '$') {
							// convert to binary
							switch(j) {
							case(0):
								rd = dec_to_bin(Integer.parseInt(data[j].substring(1)),5);
								break;
							case(1):
								rs1 = dec_to_bin(Integer.parseInt(data[j].substring(1)),5);
								break;
							case(2):
								rs2 = dec_to_bin(Integer.parseInt(data[j].substring(1)),5);
								break;
							default:
								rs3 = dec_to_bin(Integer.parseInt(data[j].substring(1)),5);
								break;
							}
						}
						else {
							if(ins.equals("shlhi")) {
								imm1 = dec_to_bin(Integer.parseInt(data[j]),5);
							}
							else if (ins.equals("li")) {
								imm1 = dec_to_bin(Integer.parseInt(data[j]),16);
								imm2 = dec_to_bin(Integer.parseInt(data[j+1]),3);
								j++;
							}
						}
					}
				}
				// put binary values together to make instruction in binary 
				switch(ins) {
				// mult add/mult sub
				case "mal":
				case "mah":	
				case "msl":
				case "msh":
					output = output + rs3 + rs2 + rs1  + rd;
					break;
				// load imm
				case "li":
					output = output + imm2 + imm1 + rd;
					break;
				// shift left half word imm
				case "shlhi":
					output = output + imm1 + rs1 + rd;
					break;
				case "bcw":
				case "popcnth":
				case "clz":
					output = output + "00000" + rs1 + rd;
					break;
				// r-types w/ 2 registers as operands
				case "and":	
				case "or":
				case "rot":
				case "a":
				case "sfw":
				case "ah":
				case "sfh":
				case "ahs":
				case "sfhs":
				case "mpyu":
				case "absdb":
					output = output + rs2 + rs1 + rd;
					break;
				// nop and invalids
				default:
					output = output + "000000000000000";
					break;
				}
				pWriter.println(output);
			}
			pWriter.close();
			System.out.println("Process Completed");
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

// function to convert decimal to binary and pad with leading 0s
public static String dec_to_bin(int x, int pad) {
	String binary = "";
	while(x > 0) {
		binary = (x%2) + binary;
		x = x/2;
	}
	for(int i = pad - binary.length(); i > 0 ; i--) {
		binary = "0" + binary;
	}
	return binary;
}

// takes string containing instruction and converts to binary
public static String ins_to_bin(String ins) {
	String out = null;
	switch(ins) {
	case "mal":
		out = "10000";
		break;
	case "mah":
		out = "10001";
		break;
	case "msl":
		out = "10010";
		break;
	case "msh":
		out = "10011";
		break;
	case "li":
		out = "0";
		break;
	case "bcw":
		out = "1100000001";
		break;
	case "and":
		out = "1100000010";
		break;
	case "or":
		out = "1100000011";
		break;
	case "popcnth":
		out = "1100000100";
		break;
	case "clz":
		out = "1100000101";
		break;
	case "rot":
		out = "1100000110";
		break;
	case "shlhi":
		out = "1100000111";
		break;
	case "a":
		out = "1100001000";
		break;
	case "sfw":
		out = "1100001001";
		break;
	case "ah":
		out = "1100001010";
		break;
	case "sfh":
		out = "1100001011";
		break;
	case "ahs":
		out = "1100001100";
		break;
	case "sfhs":
		out = "1100001101";
		break;
	case "mpyu":
		out = "1100001110";
		break;
	case "absdb":
		out = "1100001111";
		break;
	default:
		out = "1100000000";
		break;
	}
	return out;
}

}
