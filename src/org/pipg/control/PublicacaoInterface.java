package org.pipg.control;

import android.content.Context;

public interface PublicacaoInterface<Tipo> {
	public void baixaPublicacao(Boolean atualizacaoCompleta, Context ctx);
	
	public PublicacaoInterface<Tipo> getInstance();
}
