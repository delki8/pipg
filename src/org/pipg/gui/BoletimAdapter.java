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
		
		BoletimHolder bHolder;
		
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.boletim_linha, parent, false);
			
			bHolder = new BoletimHolder();
			bHolder.pastoral = (TextView) convertView.findViewById(R.id.pastoral);
			bHolder.dataPublicacao = (TextView) convertView.findViewById(R.id.dataPublicacao);
			
			bHolder.boletim = lista.get(position);
			convertView.setTag(bHolder);
		
		} else {
			bHolder = (BoletimHolder) convertView.getTag();
			bHolder.boletim = lista.get(position);
		}
		
		bHolder.pastoral.setText(bHolder.boletim.getPastoral());
		bHolder.dataPublicacao.setText(bHolder.boletim.getDataFormatada());

		return convertView;
	}
	
    private static class BoletimHolder {
    	TextView pastoral;
    	TextView dataPublicacao;
    	
    	Boletim boletim;
    }
}
