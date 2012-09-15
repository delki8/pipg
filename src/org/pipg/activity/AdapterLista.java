package org.pipg.activity;

import java.util.ArrayList;

import org.pipg.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterLista extends BaseAdapter {
	
	private ArrayList<ItemLista> itens;
	private LayoutInflater mInflater;
	
	public AdapterLista(Context context, ArrayList<ItemLista> itens) {
		this.itens = itens;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return itens.size();
	}

	@Override
	public ItemLista getItem(int position) {
		return itens.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ItemLista item = itens.get(position);
		view = mInflater.inflate(R.layout.item_lista_layout, null);
		((TextView) view.findViewById(R.id.text)).setText(item.getTexto());
		//((ImageView) view.findViewById(R.id.imagemview)).setImageResource(item.getThumbnail());
		
		return view;
	}
}
