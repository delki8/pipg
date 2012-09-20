package org.pipg.midia;

import java.net.URL;
import java.util.Date;


public class Boletim extends Publicacao{
	private String pastoral;
	private Date dataBoletim;
	private URL link;
	private Integer numeroDoBoletim;

	public String getPastoral() {
		return pastoral;
	}
	public void setPastoral(String pastoral) {
		this.pastoral = pastoral;
	}
	public Date getDataBoletim() {
		return dataBoletim;
	}
	public void setDataBoletim(Date dataBoletim) {
		this.dataBoletim = dataBoletim;
	}
	public URL getLink() {
		return link;
	}
	public void setLink(URL link) {
		this.link = link;
	}
	public Integer getNumeroDoBoletim() {
		return numeroDoBoletim;
	}
	public void setNumeroDoBoletim(Integer numeroDoBoletim) {
		this.numeroDoBoletim = numeroDoBoletim;
	}
}
