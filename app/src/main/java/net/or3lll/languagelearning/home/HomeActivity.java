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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.VocabularyTestActivity;
import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.LangListActivity;
import net.or3lll.languagelearning.configuration.word.WordListActivity;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.settings.SettingsActivity;
import net.or3lll.languagelearning.shared.UserLangAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG_ADVICE_DIALOG = "advice_dialog";

    @BindView(R.id.nav) ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.lang_src) Spinner mLangSrc;
    @BindView(R.id.lang_dst) Spinner mLangDst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        addDrawerItems();
        mDrawerList.setOnItemClickListener((parent, view, position, id) -> {
            // Todo: ne pas gérer ça avec la position
            if (position == 0) {
                Intent testIntent = new Intent(HomeActivity.this, LangListActivity.class);
                startActivity(testIntent);
            } else if (position == 1) {
                Intent testIntent = new Intent(HomeActivity.this, WordListActivity.class);
                startActivity(testIntent);
            } else if (position == 2) {
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            } else {
                Toast.makeText(HomeActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }

            mDrawerLayout.closeDrawer(Gravity.LEFT);
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();

        if(SugarRecord.count(Lang.class) < 2) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(TAG_ADVICE_DIALOG);
            if (prev != null) {
                ft.remove(prev);
            }

            DialogFragment newFragment = LangMinimumAdviceDialogFragment.newInstance();
            newFragment.show(ft, TAG_ADVICE_DIALOG);
        } else {
            setSpinnerLangs();
        }
    }

    private void setSpinnerLangs() {
        mLangSrc.setAdapter(new UserLangAdapter());
        mLangDst.setAdapter(new UserLangAdapter());
    }

    @OnClick(R.id.test_launch)
    public void onTestButtonClick() {
        Lang langSrc = (Lang) mLangSrc.getSelectedItem();
        Lang langDst = (Lang) mLangDst.getSelectedItem();
        if (langSrc != null && langDst != null && langSrc.getId() != langDst.getId()) {
            Intent testIntent = new Intent(HomeActivity.this, VocabularyTestActivity.class);
            testIntent.putExtra(VocabularyTestActivity.BUNDLE_LANG_SRC, langSrc);
            testIntent.putExtra(VocabularyTestActivity.BUNDLE_LANG_DST, langDst);
            startActivity(testIntent);
        }
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Intent testIntent = new Intent(HomeActivity.this, WordListActivity.class);
        startActivity(testIntent);
    }

    private void addDrawerItems() {
        String[] osArray = { "Langues", "Mots", "Paramètres" };
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    }
}