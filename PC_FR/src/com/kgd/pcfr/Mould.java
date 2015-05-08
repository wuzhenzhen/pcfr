package com.kgd.pcfr;

public class Mould {
	private String MId;
	private String MValue;
	private String rowData;

	public Mould() {

	}

	public Mould(String id, String value) {
		MId = id;
		MValue = value;
	}

	public String getMId() {
		return MId;
	}

	public void setMId(String mId) {
		MId = mId;
	}

	public String getMValue() {
		return MValue;
	}

	public void setMValue(String mValue) {
		MValue = mValue;
	}

	public String getString() {
		rowData = MId + " " + MValue;
		return rowData;
	}

}