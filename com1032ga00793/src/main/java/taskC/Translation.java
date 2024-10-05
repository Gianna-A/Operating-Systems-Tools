package taskC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Translation {
	private ArrayList<Address> addresses;
	private ArrayList<TLB> TLBList;
	private ArrayList<PageTable> PTList;
	private int diskNumber = 13;

	public Translation(ArrayList<Address> addresses, ArrayList<TLB> TLBList, ArrayList<PageTable> PTList) {
		this.addresses = addresses;
		this.TLBList = TLBList;
		this.PTList = PTList;

	}

	public void updateLRU(int i) {
		TLB currentTLB = null;
		currentTLB = TLBList.get(i);
		while (isRepeats()) {
			for (int j = 0; j < TLBList.size(); j++) {
				if (currentTLB != TLBList.get(j)) {
					if (currentTLB.getLRU() == TLBList.get(j).getLRU()) {
						int currentLRU = TLBList.get(j).getLRU() - 1;
						TLBList.get(j).setLRU(currentLRU);
						currentTLB = TLBList.get(j);
					}
				}
			}
		}

	}

	private boolean isRepeats() {
		for (int i = 0; i < TLBList.size(); i++) {
			for (int j = i + 1; j < TLBList.size(); j++) {
				if (TLBList.get(i).getLRU() == TLBList.get(j).getLRU()) {
					return true;
				}
			}

		}
		return false;
	}

	public void tlbTranslation() {
		for (Address address : addresses) {
			int pageNumber = address.getvPageNumber();
			boolean gotHit = false;

			for (int i = 0; i < TLBList.size(); i++) {
				if (((TLBList.get(i).getTag() == pageNumber) && (TLBList.get(i).getValid() == 1))) {
					// tlb hit
					address.setResult("Hit");
					gotHit = true;
					TLBList.get(i).setLRU(4);
					updateLRU(i);
					write_to_file(address);
				}
			}
			if (!gotHit) {
				for (int i = 0; i < PTList.size(); i++) {
					if (PTList.get(i).getIndex() == pageNumber) {
						if (PTList.get(i).getValid() == 1) {
							// TLB miss
							address.setResult("Miss");
							replaceTLB(PTList.get(i));
							updateLRU(TLBList.size() - 1);
							write_to_file(address);
						} else if (PTList.get(i).getValid() == 0) {
							// page fault
							address.setResult("Page fault");
							PTList.get(i).setPhysicalPageOrDisk(diskNumber);
							diskNumber++;
							PTList.get(i).setValid(1);
							replaceTLB(PTList.get(i));
							updateLRU(TLBList.size() - 1);
							write_to_file(address);
						}
						break;
					}
				}
			}
		}
	}

	public void replaceTLB(PageTable page) {
		for (int i = 0; i < TLBList.size(); i++) {
			if (TLBList.get(i).getLRU() == 1) {
				TLBList.remove(i);
				TLB switched = new TLB(page.getValid(), page.getIndex(), (int) page.getPhysicalPageOrDisk(), 4);
				TLBList.add(switched);
				break;

			}
		}
	}

	public void write_to_file(Address address) {
		boolean toAppend = true;
		try {
			if (address.equals(addresses.get(0))) {
				toAppend = false;
			}
			BufferedWriter bWriter = new BufferedWriter(new FileWriter("taskc-sampleoutput.txt", toAppend));
			bWriter.write("# After the memory access " + address.getAddress() + "\n");
			bWriter.write("#Address, Result (Hit, Miss, PageFault)\n");
			bWriter.write(address.getAddress() + "," + address.getResult() + "\n");
			bWriter.write("#updated TLB\n");
			bWriter.write("#Valid, Tag, Physical Page #, LRU\n");
			for (TLB tlb : TLBList) {
				bWriter.write(tlb.toString() + "\n");
			}
			bWriter.write("#updated Page table\n");
			bWriter.write("#Index,Valid,Physical Page or On Disk\n");
			for (PageTable page : PTList) {
				bWriter.write(page.toString() + "\n");
			}
			bWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
