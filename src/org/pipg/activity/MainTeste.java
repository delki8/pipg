package org.pipg.activity;

import org.pipg.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainTeste extends Activity{
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		Intent intent = new Intent("BOLETIM");
		startService(intent);
		setContentView(R.layout.pagina);
	}
}
