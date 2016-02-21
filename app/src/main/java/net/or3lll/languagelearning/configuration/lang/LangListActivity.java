package net.or3lll.languagelearning.configuration.lang;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.data.Lang;

public class LangListActivity extends AppCompatActivity
        implements LangRecyclerViewAdapter.OnClickListener,
        EditLangFragment.OnFragmentInteractionListener,
        DeleteLangDialogFragment.OnDeleteLangListener {

    private LangRecyclerViewAdapter mLangAdapter;
    
    private FrameLayout editContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_lang);
        ab.setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mLangAdapter = new LangRecyclerViewAdapter(Lang.listAll(Lang.class), this);
        recyclerView.setAdapter(mLangAdapter);

        editContainer = (FrameLayout) findViewById(R.id.edit_container);
/*        if(editContainer != null) {
            EditLangFragment fragment = EditLangFragment.newInstance(-1);
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLangAdapter.setLangs(Lang.listAll(Lang.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lang, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_lang) {
            if(editContainer != null) {
                EditLangFragment fragment = EditLangFragment.newInstance(-1, this);
                getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, fragment).commit();
            }
            else {
                Intent i = new Intent(this, EditLangActivity.class);
                startActivity(i);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Lang lang) {
        if(editContainer != null) {
            EditLangFragment fragment = EditLangFragment.newInstance(lang.getId(), this);
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, fragment).commit();
        }
        else {
            Intent i = new Intent(this, EditLangActivity.class);
            i.putExtra(EditLangActivity.LANG_ID_PARAM, lang.getId());
            startActivity(i);
        }
    }

    @Override
    public void onLongClick(Lang lang) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        DialogFragment newFragment = DeleteLangDialogFragment.newInstance(lang.getId());
        newFragment.show(ft, "dialog");
    }

    @Override
    public void onLanguageAdded() {
        mLangAdapter.setLangs(Lang.listAll(Lang.class));
    }

    @Override
    public void onLangDeleted(Lang lang) {
        mLangAdapter.setLangs(Lang.listAll(Lang.class));
    }
}
