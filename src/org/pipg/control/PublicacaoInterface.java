package org.pipg.control;

import java.util.ArrayList;

public interface PublicacaoInterface<Tipo> {
	ArrayList<Tipo> baixaPublicacao(Boolean atualizacaoCompleta);
}
