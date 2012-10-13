package org.pipg.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.pipg.beans.Boletim;

import android.app.ListActivity;
import android.os.Bundle;

public class Main extends ListActivity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		List<Boletim> boletins = pegaListaBoletim();
		setListAdapter(new BoletimAdapter(this, boletins));
	}

	private List<Boletim> pegaListaBoletim() {
		List<Boletim> boletins = new ArrayList<Boletim>();
		
		Boletim boletim1 = new Boletim();
		Boletim boletim2 = new Boletim();
		Boletim boletim3 = new Boletim();
		
		boletim1.setPastoral("pastoral boletim 1");
		boletim2.setPastoral("pastoral boletim 2");
		boletim3.setPastoral("pastoral boletim 3");
		
		boletim1.setDataPublicacao(new Date());
		boletim2.setDataPublicacao(new Date());
		boletim3.setDataPublicacao(new Date());
		
		boletins.add(boletim1);
		boletins.add(boletim2);
		boletins.add(boletim3);
		
		return boletins;
	}
}
