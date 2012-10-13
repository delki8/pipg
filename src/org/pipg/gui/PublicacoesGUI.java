package org.pipg.gui;

import java.util.ArrayList;

import org.pipg.R;
import org.pipg.beans.Boletim;
import org.pipg.control.BoletimServico;
import org.pipg.midia.ItemLista;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PublicacoesGUI extends FragmentActivity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_gui);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_teste_gui, menu);
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

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public class DummySectionFragment extends Fragment{ //implements OnItemClickListener {
        
    	AdapterLista adapter;
    	ListView lista;
    	ArrayList<ItemLista> itens;
    	ArrayList<Boletim> boletins;
    	
    	public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
        	ItemLista item1 = new ItemLista("Item1");
        	ItemLista item2 = new ItemLista("Item2");
        	ItemLista item3 = new ItemLista("Item3");
        	ItemLista item4 = new ItemLista("Item4");
        	ItemLista item5 = new ItemLista("Item5");
        	ItemLista item6 = new ItemLista("Item6");
        	ItemLista item7 = new ItemLista("Item7");
        	ItemLista item8 = new ItemLista("Item8");
        	ItemLista item9 = new ItemLista("Item9");
        	ItemLista item10 = new ItemLista("Item10");
        	ItemLista item11 = new ItemLista("Item11");
        	ItemLista item12 = new ItemLista("Item12");
        	ItemLista item13 = new ItemLista("Item13");
        	ItemLista item14 = new ItemLista("Item14");
        	ItemLista item15 = new ItemLista("Item15");
        	ItemLista item16 = new ItemLista("Item16");
        	
        	/*ItemLista item1 = new ItemLista("Item1",R.drawable.ic_pipg);
        	ItemLista item2 = new ItemLista("Item2",R.drawable.ic_pipg);
        	ItemLista item3 = new ItemLista("Item3",R.drawable.ic_pipg);
        	ItemLista item4 = new ItemLista("Item4",R.drawable.ic_pipg);
        	ItemLista item5 = new ItemLista("Item5",R.drawable.ic_pipg);
        	ItemLista item6 = new ItemLista("Item6",R.drawable.ic_pipg);
        	ItemLista item7 = new ItemLista("Item7",R.drawable.ic_pipg);
        	ItemLista item8 = new ItemLista("Item8",R.drawable.ic_pipg);
        	ItemLista item9 = new ItemLista("Item9",R.drawable.ic_pipg);
        	ItemLista item10 = new ItemLista("Item10",R.drawable.ic_pipg);
        	ItemLista item11 = new ItemLista("Item11",R.drawable.ic_pipg);
        	ItemLista item12 = new ItemLista("Item12",R.drawable.ic_pipg);
        	ItemLista item13 = new ItemLista("Item13",R.drawable.ic_pipg);
        	ItemLista item14 = new ItemLista("Item14",R.drawable.ic_pipg);
        	ItemLista item15 = new ItemLista("Item15",R.drawable.ic_pipg);
        	ItemLista item16 = new ItemLista("Item16",R.drawable.ic_pipg);
        	*/
        	
        	itens = new ArrayList<ItemLista>();
        	itens.add(item1);
        	itens.add(item2);
        	itens.add(item3);
        	itens.add(item4);
        	itens.add(item5);
        	itens.add(item6);
        	itens.add(item7);
        	itens.add(item8);
        	itens.add(item9);
        	itens.add(item10);
        	itens.add(item11);
        	itens.add(item12);
        	itens.add(item13);
        	itens.add(item14);
        	itens.add(item15);
        	itens.add(item16);
        	
        	BoletimServico boletimServico = new BoletimServico();
        	boletins = boletimServico.baixaPublicacao(false);
        	
        	adapter = new AdapterLista(getActivity(), boletins);
        	lista = new ListView(getActivity());
        	lista.setAdapter(adapter);
        	
            //TextView textView = new TextView(getActivity());
            //textView.setGravity(Gravity.CENTER);
            //Bundle args = getArguments();
            //textView.setText(TESTE);
            //textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            return lista;
        }

	/*@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ItemLista item = adapter.getItem(arg2);
		
		item.setTexto("clicado");
		
		Log.d("onItemClick", item.getTexto());
		*/
		
		
		
		
	}
    
}
