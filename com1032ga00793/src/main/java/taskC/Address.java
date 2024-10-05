package taskC;

public class Address {
	private String address;
	private int vPageNumber;
	private String result;

	public Address(String address, int vPageNumber, String result) {
		this.address = address;
		this.vPageNumber = vPageNumber;
		this.result = result;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getvPageNumber() {
		return vPageNumber;
	}

	public void setvPageNumber(int vPageNumber) {
		this.vPageNumber = vPageNumber;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}