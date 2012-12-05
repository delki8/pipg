package org.pipg.gui;

import org.pipg.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class Sobre extends FragmentActivity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.sobre);

		// Tratando informação sobre desenvolvedores
		TextView txtDev = (TextView) findViewById(R.id.sobre_dev);
		String dev = getString(R.string.dev_1) + " e "
				+ getString(R.string.dev_2);
		txtDev.setText(dev);
	}
}
