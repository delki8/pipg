package org.pipg.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.pipg.R;
import org.pipg.beans.Boletim;
import org.pipg.control.BoletimControl;
import org.pipg.net.BoletimRepositorio;
import org.pipg.net.DownloaderThread;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class PublicacoesGUI extends FragmentActivity 
	implements ActionBar.TabListener {
	
	public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
	public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
	public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
	public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
	public static final int MESSAGE_CONNECTING_STARTED = 1004;
	public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;
	public static final int MESSAGE_TERMINOU_UPDATE = 1006;
	public static final int MESSAGE_TERMINOU_LIMPAR = 1007;
	
	private static final String LOG = "pipg";

	private static PublicacoesGUI thisActivity;
	private ProgressDialog progressDialog;

	private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    
	private static BoletimAdapter adapter;
	private static ListView lista;
	private static ArrayList<Boletim> boletins;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_gui);
        thisActivity = this;
        progressDialog = null;
    	
        mSectionsPagerAdapter = new SectionsPagerAdapter(
        		getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager
        		.SimpleOnPageChangeListener() {
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
        MenuItem refreshParcial = menu.findItem(R.id.menu_refresh_parcial);
        refreshParcial.setOnMenuItemClickListener(
        		new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				new Thread(){
					@Override
					public void run() {
						BoletimControl bControl = new BoletimControl();
						bControl.atualizaBoletins(false, thisActivity);
					}
				}.start();
				return true;
			}
		});
        
        MenuItem refreshCompleto = menu.findItem(R.id.menu_refresh_completo);
        refreshCompleto.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	
        	@Override
        	public boolean onMenuItemClick(MenuItem item) {
        		BoletimControl bControl = new BoletimControl();
	        	bControl.atualizaBoletins(true, thisActivity);
	        	return true;
        	}
        });
        
        MenuItem limpar = menu.findItem(R.id.menu_limpar);
        limpar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	
        	@Override
        	public boolean onMenuItemClick(MenuItem item) {
        		BoletimRepositorio bRepositorio = new BoletimRepositorio(
        				thisActivity);
        		int linhasApagadas = bRepositorio.limparBanco();
        		Toast.makeText(thisActivity, linhasApagadas + 
        				" registros apagados", Toast.LENGTH_SHORT).show();
        		bRepositorio.fechar();
        		atualizaAdapter(new ArrayList<Boletim>());
        		return true;
        	}
        });
        
        MenuItem inserir = menu.findItem(R.id.menu_inserir);
        inserir.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	
        	@Override
        	public boolean onMenuItemClick(MenuItem item) {
        		BoletimRepositorio bRepositorio = new BoletimRepositorio(thisActivity);
        		ArrayList<Boletim> boletins = new ArrayList<Boletim>();
        		try {
	        		for (int i = 0; i < 25; i++) {
	        			Boletim b = new Boletim();
	        			b.setPastoral("Pastoral Teste " + i);
						b.setLink(new URL("http://www.google.com"));
	        			b.setDataPublicacao(new Date());
	        			boletins.add(b);
					}
        		} catch (MalformedURLException e) {
        			e.printStackTrace();
        		}
        		bRepositorio.inserir(boletins);
        		bRepositorio.fechar();
        		return true;
        	}
        });
        
        return true;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, 
    		FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, 
    		FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, 
    		FragmentTransaction fragmentTransaction) {
    }
    
    private void atualizaAdapter(Collection<Boletim> bols) {
		boletins.clear();
		boletins.addAll(bols);
//		adapter.setLista(boletins);
		ListView l = (ListView) findViewById(SectionFragment.ID_MAIN_LIST);
		l.setAdapter(adapter);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_boletins)
                		.toUpperCase();
                case 1: return getString(R.string.title_audios)
                		.toUpperCase();
                case 2: return getString(R.string.title_videos)
                		.toUpperCase();
            }
            return null;
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return super.onOptionsItemSelected(item);
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class SectionFragment extends Fragment { //implements OnItemClickListener {
    	public SectionFragment() {};
    	
        public static final String ARG_SECTION_NUMBER = "section_number";
        public static final int ID_MAIN_LIST = 2000;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
			BoletimRepositorio bRepositorio = new BoletimRepositorio(getActivity());
			boletins = bRepositorio.listarBoletins();

        	adapter = new BoletimAdapter(thisActivity, boletins);
        	lista = new ListView(getActivity());
        	lista.setId(ID_MAIN_LIST);
        	lista.setAdapter(adapter);

            return lista;
        }
	}
    
	/**
	 * This is the Handler for this acitivy. It will receive messages from the
	 * {@link DownloaderThread} and make the necessary updates to the UI.
	 * */
	public Handler activityHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * Handling MESSAGE_UPDATE_PROGRESS_BAR:
			 * 1. Get the current prorfess, as indicated in arg1 field
			 * of the Message.
			 * 2. Update the progress bar.
			 * */
			case MESSAGE_UPDATE_PROGRESS_BAR:
				if (progressDialog != null) {
					int currentProgress = msg.arg1;
					progressDialog.setProgress(currentProgress);
				}
				break;
			/*
			 * Handling MESSAGE_CONNECTING_STARTED:
			 * 1. Get the URL of the file being downloaded. This is stored
			 * in the obj field of the Message.
			 * 2. Create an indeterminate progress bar;
			 * 3. Show the progress bar.
			 * */
			case MESSAGE_CONNECTING_STARTED:
				if (msg.obj != null && msg.obj instanceof String) {
					String url = (String) msg.obj;
					// truncate the url
					if (url.length() > 16) {
						String tUrl = url.substring(0, 15);
						tUrl += "...";
						url = tUrl;
					}
					String pdTitle = thisActivity.getString(
							R.string.progress_dialog_title_connecting);
					String pdMsg = thisActivity.getString(
							R.string.progress_dialog_message_prefix_connecting);
					pdMsg += " " + url;
					
					dismissCurrentProgressDialog();
					progressDialog = new ProgressDialog(thisActivity);
					progressDialog.setTitle(pdTitle);
					progressDialog.setMessage(pdMsg);
					progressDialog.setProgressStyle(
							ProgressDialog.STYLE_SPINNER);
					progressDialog.setIndeterminate(true);
					// set the message to be sent when this dialog is canceled
					Message newMsg = Message.obtain(this, 
							MESSAGE_DOWNLOAD_CANCELED);
					progressDialog.setCancelMessage(newMsg);
					progressDialog.show();
				}
				break;
			/*
			 * Handling MESSAGE_DOWNLOAD_STARTED:
			 * 1. Create a progress bar with specified max value and current
			 * value 0; assign it to progressDialog. The arg1 field will
			 * contain the max value.
			 * 2. Set the title and text for the progress bar. The obj
			 * field of the Message will contain a String that
			 * represents the name of the file being downloaded.
			 * 3. Set the message that shoud be sent if dialog is canceled.
			 * 4. Make the progress bar visible.
			 * */
			case MESSAGE_DOWNLOAD_STARTED:
				// obj will contain a String representing the file name
				if (msg.obj != null && msg.obj instanceof String) {
					int maxValue = msg.arg1;
					String fileName = (String) msg.obj;
					String pdTitle = thisActivity.getString(
							R.string.progress_dialog_title_downloading);
					String pdMsg = thisActivity.getString(
							R.string.progress_dialog_message_prefix_downloading);
					pdMsg += " " + fileName;
					
					dismissCurrentProgressDialog();
					progressDialog = new ProgressDialog(thisActivity);
					progressDialog.setTitle(pdTitle);
					progressDialog.setMessage(pdMsg);
					progressDialog.setProgressStyle(
							ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setProgress(0);
					progressDialog.setMax(maxValue);
					// set the message to be sent when this dialog is canceled
					Message newMsg = Message.obtain(this, 
							MESSAGE_DOWNLOAD_CANCELED);
					progressDialog.setCancelMessage(newMsg);
					progressDialog.setCancelable(true);
					progressDialog.show();
				}
				break;
			/*
			 * Handling MESSAGE_DOWNLOAD_COMPLETE
			 * 1. Remove the progress bar from the screen.
			 * 2. Display Toast that says download is complete.
			 * */
			case MESSAGE_DOWNLOAD_COMPLETE:
				dismissCurrentProgressDialog();
				displayMessage(getString(
						R.string.user_message_download_complete));
				break;
			/*
			 * Handling MESSAGE_DOWNLOAD_CANCELED:
			 * 1. Interrupt the downloader thread.
			 * 2. Remove the progress bar from the screen.
			 * 3. Display Toast that says download is complete.
			 * */
			case MESSAGE_DOWNLOAD_CANCELED:
//				if (downloaderThread != null) {
//					downloaderThread.interrupt();
//				}
//				dismissCurrentProgressDialog();
//				displayMessage(getString(
//						R.string.user_message_download_canceled));
				break;
			/*
			 * Handling MESSAGE_ENCOUNTERED_ERROR:
			 * 1. Check the obj field of the message for the actual error
			 * message that will be displayed to the user.
			 * 2. Remove any progress bars from the screen.
			 * 3. Display a Toast with the error message.
			 * */
			case MESSAGE_ENCOUNTERED_ERROR:
				// obj will contain a string representing the error message
				if (msg.obj != null && msg.obj instanceof String) {
					String errorMessage = (String) msg.obj;
					dismissCurrentProgressDialog();
					displayMessage(errorMessage);
				}
				break;
			/*
			 * Tratando MESSAGE_TERMINOU_UPDATE
			 * 1. Instancia o repositorio de boletins e atualiza a lista
			 *	de boletins recém cadastrados.
			 * 2. Pega a nova lista de boletins e manda para o método de 
			 * 	atualização da tela.
			 * */
			case MESSAGE_TERMINOU_UPDATE:
				BoletimRepositorio br = new BoletimRepositorio(thisActivity);
				List<Boletim> bols = br.listarBoletins();
				atualizaAdapter(bols);
				break;
				
			case MESSAGE_TERMINOU_LIMPAR:
				atualizaAdapter(new ArrayList<Boletim>());
				break;
			default:
				// nothing to do here
				break;
			}
		}
	};
	
	/**
	 * If there is a progress dialog, dismiss it and set progressDialog 
	 * to null.
	 * */
	public void dismissCurrentProgressDialog() {
		if (progressDialog != null) {
			progressDialog.hide();
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	/**
	 * Display a message to the user, in the form of a Toast.
	 * @param message Message to be displayed.
	 * */
	public void displayMessage(String message) {
		if (message != null) {
			Toast.makeText(thisActivity, message, Toast.LENGTH_SHORT).show();
		}
	}
}
