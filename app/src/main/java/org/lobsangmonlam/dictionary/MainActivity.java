package org.lobsangmonlam.dictionary;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.Duration;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import org.ironrabbit.type.CustomTypefaceManager;
import org.ironrabbit.type.CustomTypefaceTextView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener {

    private FragmentTabHost mTabHost;
    private Toolbar mToolbar;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomTypefaceManager.loadFromAssets(this);

        setContentView(R.layout.tabs);

        setTitle(getString(R.string.main_title));
        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(mToolbar);
        getActionBarTextView().setTypeface(CustomTypefaceManager.getCurrentTypeface(this));

        mToolbar.setLogo(R.drawable.toolbaricon);
        mToolbar.setCollapsible(true);
        mToolbar.setTitle(getTitle());

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tab) {
                mTabHost.clearFocus();

                /**
                 if (mTabHost.getCurrentView() != null) {

                 View view = mTabHost.getCurrentView().getRootView().findViewById(R.id.searchbox);

                 if (view != null)
                 view.requestFocusFromTouch();
                 }*/
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("db", "tbtotb");
        bundle.putInt("dbresid", R.raw.tbtotb);
        mTabHost.addTab(mTabHost.newTabSpec("tbtotb").setIndicator(getString(R.string.tab_tb)),
                SearchFragment.class, bundle);


        bundle = new Bundle();
        bundle.putString("db", "entotb");
        bundle.putInt("dbresid", R.raw.entotb);
        mTabHost.addTab(mTabHost.newTabSpec("entotb").setIndicator(getString(R.string.tab_entotb)),
                SearchFragment.class, bundle);


        bundle = new Bundle();
        bundle.putString("db", "tbtoen");
        bundle.putInt("dbresid", R.raw.tbtoen);
        mTabHost.addTab(mTabHost.newTabSpec("tbtoen").setIndicator(getString(R.string.tab_tbtoen)),
                SearchFragment.class, bundle);


        TabWidget tw = (TabWidget) mTabHost.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getTabCount(); i++) {
            View tabView = tw.getChildTabViewAt(i);
            TextView tv = (TextView) tabView.findViewById(android.R.id.title);
            tv.setTypeface(CustomTypefaceManager.getCurrentTypeface(this));
        }

        checkFirstTime();

        checkUpdates();
    }

    private void checkUpdates ()
    {
        try {
            AppUpdater appUpdater = new AppUpdater(this);
            appUpdater.setDisplay(Display.DIALOG);
            appUpdater.setUpdateFrom(UpdateFrom.XML);
            appUpdater.setUpdateXML(MonlamConstants.URL_UPDATER);
          //  appUpdater.showAppUpdated(true);
            appUpdater.start();
        }
        catch (Exception e)
        {
            Log.d("AppUpdater","error checking app updates",e);
        }
	}

    private void checkFirstTime ()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("showapps",true))
        {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getString(R.string.app_prompt_title));
         //   alert.setMessage(getString(R.string.app_prompt));

            final CustomTypefaceTextView input = new CustomTypefaceTextView (this);
            input.setText(getString(R.string.app_prompt));
            input.setPadding(35,10,35,10);
            alert.setView(input);


            alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    showApps();
                }
            });

            alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });
            alert.show();

            prefs.edit().putBoolean("showapps",false).commit();
        }
    }

    private TextView getActionBarTextView() {
        TextView titleTextView = null;

        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(mToolbar);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        return titleTextView;
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showApps ()
    {
        //show list of recommended apps
        startActivity(new Intent(this,AppListActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_apps:
                showApps();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}