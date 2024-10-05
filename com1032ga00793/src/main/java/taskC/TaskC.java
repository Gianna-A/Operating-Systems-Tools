package taskC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TaskC {
	private static String filePath = "taskC.txt";
	private static ArrayList<Address> addresses;
	private static ArrayList<TLB> TLBList;
	private static ArrayList<PageTable> PTList;

	public static void readAddresses() {
		addresses = new ArrayList<Address>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = bReader.readLine()) != null) {
				if (line.startsWith("0x")) {
					int pageNumber = Integer.parseInt(String.valueOf(line.charAt(2)));
					Address address = new Address(line, pageNumber, null);
					addresses.add(address);
				}
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readTLB() {
		boolean startParsing = false;
		TLBList = new ArrayList<TLB>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = bReader.readLine()) != null) {
				if (startParsing && !line.startsWith("#Initial Page table")) {
					String[] trimmedField = line.split(",");
					int valid = Integer.parseInt(trimmedField[0].trim());
					int tag = Integer.parseInt(trimmedField[1].trim());
					int physicalPage = Integer.parseInt(trimmedField[2].trim());
					int LRU = Integer.parseInt(trimmedField[3].trim());
					TLB tlb = new TLB(valid, tag, physicalPage, LRU);
					TLBList.add(tlb);
				} else if (line.startsWith("#Valid")) {
					startParsing = true;
					continue;
				} else if (line.startsWith("#Initial Page table")) {
					break;
				}
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readPageTable() {
		boolean startParsing = false;
		PTList = new ArrayList<PageTable>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = bReader.readLine()) != null) {
				if (startParsing) {
					String[] trimmedField = line.split(",");
					int index = Integer.parseInt(trimmedField[0].trim());
					int valid = Integer.parseInt(trimmedField[1].trim());
					Object physicalPageOrDisk;
					if (trimmedField[2].trim().equals("Disk")) {
						physicalPageOrDisk = "Disk";
					} else {
						physicalPageOrDisk = Integer.parseInt(trimmedField[2].trim());
					}
					PageTable entry = new PageTable(index, valid, physicalPageOrDisk);
					PTList.add(entry);
				} else if (line.startsWith("#Index")) {
					startParsing = true;
					continue;
				}
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void translate() {
		Translation translation = new Translation(addresses, TLBList, PTList);
		translation.tlbTranslation();
	}

	public static void main(String[] args) {
		readTLB();

		readPageTable();

		readAddresses();

		translate();

	}

}
