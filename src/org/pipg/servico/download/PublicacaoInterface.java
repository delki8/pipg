package org.pipg.servico.download;

import java.util.ArrayList;

public interface PublicacaoInterface<Tipo> {
	ArrayList<Tipo> baixaPublicacao(Boolean atualizacaoCompleta);
}
