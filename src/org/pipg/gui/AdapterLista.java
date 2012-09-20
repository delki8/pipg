package org.pipg.gui;

import java.util.ArrayList;

import org.pipg.R;
import org.pipg.R.id;
import org.pipg.R.layout;
import org.pipg.midia.Boletim;
import org.pipg.midia.ItemLista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterLista extends BaseAdapter {
	
	private ArrayList<ItemLista> itens;
	private ArrayList<Boletim> boletins;
	private LayoutInflater mInflater;
	
	
	
public AdapterLista(Context context, ArrayList<Boletim> boletins) {
		super();
		this.boletins = boletins;
		mInflater =  LayoutInflater.from(context);
	}

//	public AdapterLista(Context context, ArrayList<ItemLista> itens) {
//		this.itens = itens;
//		mInflater = LayoutInflater.from(context);
//	}

	@Override
	public int getCount() {
		return boletins.size();
	}

	@Override
	public Boletim getItem(int position) {
		return boletins.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		//ItemLista item = itens.get(position);
		Boletim boletim = boletins.get(position);
		view = mInflater.inflate(R.layout.item_lista_layout, null);
		((TextView) view.findViewById(R.id.text)).setText(boletim.getPastoral());
		//((TextView) view.findViewById(R.id.text)).setText(item.getTexto());
		//((ImageView) view.findViewById(R.id.imagemview)).setImageResource(item.getThumbnail());
		
		return view;
	}
}
