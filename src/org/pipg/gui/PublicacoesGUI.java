package org.pipg.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.pipg.R;
import org.pipg.beans.Boletim;
import org.pipg.control.BoletimControl;
import org.pipg.control.BoletimServico;
import org.pipg.net.BoletimRepositorio;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
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
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    
   private static ArrayList<Boletim> boletins;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        // Create the adapter that will return a fragment for each of the three 
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
        		getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding tab.
        // We can also use ActionBar.Tab#select() to do this if we have a reference to the
        // Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(actionBar.newTab()
            		.setText(mSectionsPagerAdapter.getPageTitle(i))
            		.setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_action_gui, menu);
        
        MenuItem refreshParcial = menu.findItem(R.id.menu_refresh_parcial);
        refreshParcial.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				BoletimControl bControl = new BoletimControl();
	        	int inseridos = bControl.atualizaBoletins(false, 
	        			PublicacoesGUI.this);
	        	
	        	boolean inseriu = inseridos > 0 ? true : false;
				return inseriu;
			}
		});
        
        MenuItem refreshCompleto = menu.findItem(R.id.menu_refresh_completo);
        refreshCompleto.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	
        	@Override
        	public boolean onMenuItemClick(MenuItem item) {
        		BoletimControl bControl = new BoletimControl();
	        	int inseridos = bControl.atualizaBoletins(true, 
	        			PublicacoesGUI.this);
	        	
	        	boolean inseriu = inseridos > 0 ? true : false;
				return inseriu;
        	}
        });
        
        MenuItem limpar = menu.findItem(R.id.menu_limpar);
        limpar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	
        	@Override
        	public boolean onMenuItemClick(MenuItem item) {
        		BoletimRepositorio bRepositorio = new BoletimRepositorio(
        				PublicacoesGUI.this);
        		int linhasApagadas = bRepositorio.limparBanco();
        		Toast.makeText(PublicacoesGUI.this, linhasApagadas + 
        				" registros apagados", Toast.LENGTH_SHORT).show();
        		bRepositorio.fechar();
        		return true;
        	}
        });
        
        MenuItem inserir = menu.findItem(R.id.menu_inserir);
        inserir.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	
        	@Override
        	public boolean onMenuItemClick(MenuItem item) {
        		BoletimRepositorio bRepositorio = new BoletimRepositorio(
        				PublicacoesGUI.this);
        		ArrayList<Boletim> boletins = new ArrayList<Boletim>();
        		try {
	        		for (int i = 0; i < 10; i++) {
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
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
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
                case 0: return getString(R.string.title_boletins).toUpperCase();
                case 1: return getString(R.string.title_audios).toUpperCase();
                case 2: return getString(R.string.title_videos).toUpperCase();
            }
            return null;
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Toast.makeText(this, "Item selecionado: " + item.getItemId(), 
    			Toast.LENGTH_SHORT).show();
    	return super.onOptionsItemSelected(item);
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment { //implements OnItemClickListener {
    	BoletimAdapter adapter;
    	ListView lista;
    	
    	public DummySectionFragment() {
    	}
    	
        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
			BoletimRepositorio bRepositorio = new BoletimRepositorio(getActivity());
			boletins = bRepositorio.listarBoletins();

        	adapter = new BoletimAdapter(getActivity(), boletins);
        	lista = new ListView(getActivity());
        	lista.setAdapter(adapter);

            return lista;
        }
	}
}
