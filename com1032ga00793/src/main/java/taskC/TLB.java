package taskC;

public class TLB {
	private int valid;
	private int tag;
	private int physicalPage;
	private int LRU;

	public TLB(int valid, int tag, int physicalPage, int LRU) {
		this.valid = valid;
		this.tag = tag;
		this.physicalPage = physicalPage;
		this.LRU = LRU;

	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getPhysicalPage() {
		return physicalPage;
	}

	public void setPhysicalPage(int physicalPage) {
		this.physicalPage = physicalPage;
	}

	public int getLRU() {
		return LRU;
	}

	public void setLRU(int lRU) {
		LRU = lRU;
	}

	@Override
	public String toString() {
		return valid + "," + tag + "," + physicalPage + "," + LRU;
	}
}
