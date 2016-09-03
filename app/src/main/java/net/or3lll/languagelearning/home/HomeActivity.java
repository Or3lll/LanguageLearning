package net.or3lll.languagelearning.home;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.JpFrActivity;
import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.DeleteLangDialogFragment;
import net.or3lll.languagelearning.configuration.lang.LangListActivity;
import net.or3lll.languagelearning.configuration.word.WordListActivity;
import net.or3lll.languagelearning.data.Lang;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG_ADVICE_DIALOG = "advice_dialog";

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Boutons de l'Activity
        Button testButton = (Button) findViewById(R.id.test_btn);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testIntent = new Intent(HomeActivity.this, JpFrActivity.class);
                startActivity(testIntent);
            }
        });

        // Navigation Drawer
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Todo: ne pas gérer ça avec la position
                if (position == 0) {
                    Intent testIntent = new Intent(HomeActivity.this, LangListActivity.class);
                    startActivity(testIntent);
                } else if (position == 1) {
                    Intent testIntent = new Intent(HomeActivity.this, WordListActivity.class);
                    startActivity(testIntent);
                } else {
                    Toast.makeText(HomeActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                }

                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        setupDrawer();

        if(SugarRecord.count(Lang.class) < 2) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(TAG_ADVICE_DIALOG);
            if (prev != null) {
                ft.remove(prev);
            }

            DialogFragment newFragment = LangMinimumAdviceDialogFragment.newInstance();
            newFragment.show(ft, TAG_ADVICE_DIALOG);
        }
    }

    private void addDrawerItems() {
        String[] osArray = { "Langues", "Mots" };
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_add_word:
                Intent testIntent = new Intent(HomeActivity.this, WordListActivity.class);
                startActivity(testIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }}
