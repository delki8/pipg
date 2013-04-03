package org.pipg.gui;

import java.util.ArrayList;

import org.pipg.R;
import org.pipg.beans.Boletim;
import org.pipg.control.BoletimControl;
import org.pipg.net.BoletimRepositorio;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PublicacoesGUI extends FragmentActivity implements
		ActionBar.TabListener {

	private static BoletimAdapter adapter;
	private static ListView lista;
	private static ArrayList<Boletim> boletins;
	private static PublicacoesGUI thisActivity;
	private static ProgressDialog progressDialog;

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	
	/**
	 * This is the Handler for this activity. It will receive messages from the
	 * {@link DownloaderThread} and make the necessary updates to the UI.
	 * */
	public static PublicacoesHandler activityHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gui);
		thisActivity = this;
		progressDialog = null;

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(thisActivity));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_action_gui, menu);
		return true;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new SectionFragment();
			Bundle args = new Bundle();
			args.putInt(SectionFragment.ARG_SECTION_NUMBER, i + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// return 3;
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_boletins).toUpperCase();
			case 1:
				return getString(R.string.title_audios).toUpperCase();
			case 2:
				return getString(R.string.title_videos).toUpperCase();
			}
			return null;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh_recentes:
			new Thread() {
				@Override
				public void run() {
					BoletimControl bControl = new BoletimControl();
					bControl.atualizaBoletins(false, thisActivity);
				}
			}.start();
			break;
		case R.id.menu_refresh_todos:
			new Thread() {
				@Override
				public void run() {
					BoletimControl bControl = new BoletimControl();
					bControl.atualizaBoletins(true, thisActivity);
				}
			}.start();
			break;
		case R.id.menu_limpar:
			BoletimControl bControl = new BoletimControl();
			bControl.limparBoletins(thisActivity);
			break;
		case R.id.menu_sobre:
			Intent it = new Intent(thisActivity, Sobre.class);
			startActivity(it);
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class SectionFragment extends Fragment { // implements
															// OnItemClickListener
															// {
		public SectionFragment() {
		};

		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final int ID_MAIN_LIST = 2000;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			BoletimRepositorio bRepositorio = new BoletimRepositorio(
					getActivity());
			boletins = bRepositorio.listarBoletins();

			adapter = new BoletimAdapter(thisActivity, boletins);
			lista = new ListView(getActivity());
			lista.setId(ID_MAIN_LIST);
			lista.setAdapter(adapter);
			
			// Instancia o handler que vai manipular a tela.
			activityHandler = new PublicacoesHandler(thisActivity, boletins,
					adapter, progressDialog);

			return lista;
		}
	}
}