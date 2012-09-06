package org.pipg.bean;

import java.util.Date;


public class Boletim extends Publicacao{
	private String pastoral;
	private Date dataDoBoletim;
	private String link;

	public String getPastoral() {
		return pastoral;
	}
	public void setPastoral(String pastoral) {
		this.pastoral = pastoral;
	}
	public Date getDataDoBoletim() {
		return dataDoBoletim;
	}
	public void setDataDoBoletim(Date dataDoBoletim) {
		this.dataDoBoletim = dataDoBoletim;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
