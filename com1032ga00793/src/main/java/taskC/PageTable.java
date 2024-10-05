package taskC;

public class PageTable {
	private int index;
	private int valid;
	private Object physicalPageOrDisk;

	public PageTable(int index, int valid, Object physicalPageOrDisk) {
		this.index = index;
		this.valid = valid;
		this.physicalPageOrDisk = physicalPageOrDisk;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public Object getPhysicalPageOrDisk() {
		return physicalPageOrDisk;
	}

	public void setPhysicalPageOrDisk(Object physicalPageOrDisk) {
		this.physicalPageOrDisk = physicalPageOrDisk;
	}

	@Override
	public String toString() {
		return index + "," + valid + "," + physicalPageOrDisk;
	}

}
