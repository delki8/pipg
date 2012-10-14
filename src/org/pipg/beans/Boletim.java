package org.pipg.beans;

import java.net.URL;
import java.util.Date;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class Boletim extends Publicacao{
	public static String[] colunas = new String[] {
		Boletins._ID, Boletins.PASTORAL, Boletins.DATA, Boletins.DATAPUB,
		Boletins.LINK, Boletins.NUMERO
	};
	/* 
	 * Pacote do ContentProvider. Precisa ser unico
	 * */
	public static final String AUTHORITY = "br.org.pipg.provider.boletim";
	
	private long id;
	private String pastoral;
	private Date dataBoletim;
	private URL link;
	private Integer numeroDoBoletim;
	
	public Boletim(long id, String pastoral, Date dataBoletim, URL link,
			Integer numeroDoBoletim) {
		super();
		this.id = id;
		this.pastoral = pastoral;
		this.dataBoletim = dataBoletim;
		this.link = link;
		this.numeroDoBoletim = numeroDoBoletim;
	}
	
	public Boletim() {
		super();
	}
	
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public static final class Boletins implements BaseColumns {
		private Boletins() {
		}
		
		public static final Uri CONTENT_URI = 
				Uri.parse("content://" + AUTHORITY + "/boletins");
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.google.boletins";
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.google.boletins";
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		public static final String PASTORAL = "pastoral";
		public static final String DATAPUB = "data_publicacao";
		public static final String DATA = "data";
		public static final String LINK = "link";
		public static final String NUMERO = "numero" ;
		
		public static Uri getUriId(long id) {
			Uri uriBoletim = ContentUris.withAppendedId(Boletins.CONTENT_URI, 
					id);
			return uriBoletim;
		}
	}
}