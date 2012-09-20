package org.pipg.midia;

import java.util.Date;

public abstract class Publicacao {
	private Date dataPublicacao;
	private Boolean jaFoiBaixado;
	
	public Date getDataPublicacao() {
		return dataPublicacao;
	}
	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}
	public Boolean getJaFoiBaixado() {
		return jaFoiBaixado;
	}
	public void setJaFoiBaixado(Boolean jaFoiBaixado) {
		this.jaFoiBaixado = jaFoiBaixado;
	}
}
