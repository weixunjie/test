package com.example.com.jld.facecheck.app.models;

public class RecordInfo
{
	private String rec_sex;


	private String rec_name;
	
	private String rec_idcard;

	private String rec_address;

	private String rec_birthday;

	private String rec_mz;

	private String rec_date;

	private byte[] rec_idcardimage;
	
	private byte[] rec_realtimeimage;
	public String getRec_date() {
		return rec_date;
	}

	public void setRec_date(String rec_date) {
		this.rec_date = rec_date;
	}

	public String getRec_idcard() {
		return rec_idcard;
	}

	public void setRec_idcard(String rec_idcard) {
		this.rec_idcard = rec_idcard;
	}

	
	
	public String getRec_address() {
		return rec_address;
	}

	public void setRec_address(String rec_address) {
		this.rec_address = rec_address;
	}

	public String getRec_mz() {
		return rec_mz;
	}

	
	public void setRec_mz(String rec_mz) {
		this.rec_mz = rec_mz;
	}

	public String getRec_birthday() {
		return rec_birthday;
	}

	public void setRec_birthday(String rec_birthday) {
		this.rec_birthday = rec_birthday;
	}

	public String getRec_sex() {
		return rec_sex;
	}

	public void setRec_sex(String rec_sex) {
		this.rec_sex = rec_sex;
	}

	public String getRec_name() {
		return rec_name;
	}

	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}

	public byte[] getRec_idcardimage() {
		return rec_idcardimage;
	}

	public void setRec_idcardimage(byte[] rec_idcardimage) {
		this.rec_idcardimage = rec_idcardimage;
	}

	public byte[] getRec_realtimeimage() {
		return rec_realtimeimage;
	}

	public void setRec_realtimeimage(byte[] rec_realtimeimage) {
		this.rec_realtimeimage = rec_realtimeimage;
	}

}