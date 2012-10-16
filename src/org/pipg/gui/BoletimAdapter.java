package org.pipg.gui;

import java.util.List;

import org.pipg.R;
import org.pipg.beans.Boletim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BoletimAdapter extends BaseAdapter {
	private Context context;
	private List<Boletim> lista; 
	
	public BoletimAdapter(Context context, List<Boletim> lista) {
		super();
		this.context = context;
		this.lista = lista;
	}

	@Override
	public int getCount() {
		return lista.size(); 
	}

	@Override
	public Object getItem(int position) {
		return lista.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Boletim boletim = lista.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.boletim_linha, null);
		TextView pastoral = (TextView) view.findViewById(R.id.pastoral);
		pastoral.setText(boletim.getPastoral());
		
		TextView dataPublicacao = (TextView) view.findViewById(R.id.dataPublicacao);
		dataPublicacao.setText(boletim.getDataFormatada());
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "clicou", Toast.LENGTH_SHORT);
			}
		});
		return view;
	}

}
