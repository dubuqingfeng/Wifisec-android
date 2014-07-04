package com.ndsec.wifisec;

import java.io.Serializable;

public class Params implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String note;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public void deleteNote(String note) {
		this.note = note;
	}
}
