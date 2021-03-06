package net.or3lll.languagelearning.configuration.lang.list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.GenericItemAdapter;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.orm.SugarRecord;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.lang.edit.EditLangActivity;
import net.or3lll.languagelearning.configuration.lang.edit.EditLangFragment;
import net.or3lll.languagelearning.configuration.lang.list.dialog.DefaultLangsDialogFragment;
import net.or3lll.languagelearning.configuration.lang.list.dialog.DeleteLangDialogFragment;
import net.or3lll.languagelearning.data.DataEventType;
import net.or3lll.languagelearning.data.Lang;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LangListActivity extends AppCompatActivity
        implements TableLangListener,
        DefaultLangsDialogFragment.OnDefaultLangSelected {

    private static final String TAG_EDIT_FRAGMENT = "edit_fragment";
    private static final String TAG_DELETE_DIALOG = "delete_dialog";
    private static final String TAG_ADD_DIALOG = "add_dialog";

    private GenericItemAdapter<Lang, LangItem> langAdapter;

    @BindView(R.id.emptyList) TextView emptyListText;
    @Nullable @BindView(R.id.edit_container) FrameLayout editContainer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.lang_activity_title);
        ab.setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FastAdapter<LangItem> fastAdapter = new FastAdapter<>();
        langAdapter = new GenericItemAdapter<>(LangItem::new);

        fastAdapter.withOnClickListener((v, adapter, item, position) -> {
            if(editContainer != null) {
                EditLangFragment fragment = EditLangFragment.newInstance(item.getModel());
                getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, fragment, TAG_EDIT_FRAGMENT).commit();
            }
            else {
                Intent i = new Intent(this, EditLangActivity.class);
                i.putExtra(EditLangActivity.LANG_PARAM, item.getModel());
                startActivity(i);
            }

            return true;
        });

        fastAdapter.withOnLongClickListener((v, adapter, item, position) ->  {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_DELETE_DIALOG);
            if (prev != null) {
                ft.remove(prev);
            }

            DialogFragment newFragment = DeleteLangDialogFragment.newInstance(item.getModel());
            newFragment.show(ft, TAG_DELETE_DIALOG);

            return true;
        });

        recyclerView.setAdapter(langAdapter.wrap(fastAdapter));
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateList();
    }

    private void updateList() {
        langAdapter.clear();
        langAdapter.setModel(SugarRecord.listAll(Lang.class));

        if(langAdapter.getItemCount() == 0) {
            emptyListText.setVisibility(View.VISIBLE);
        }
        else {
            emptyListText.setVisibility(View.GONE);
        }
    }

    private void hideEdit() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_EDIT_FRAGMENT);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.commit();
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG_ADD_DIALOG);
        if (prev != null) {
            ft.remove(prev);
        }

        DialogFragment newFragment = DefaultLangsDialogFragment.newInstance();
        newFragment.show(ft, TAG_ADD_DIALOG);
    }

    @Override
    public void onLangSelected(Lang lang) {
        updateList();
        hideEdit();
    }

    @Override
    public void onOtherSelected() {
        if(editContainer != null) {
            EditLangFragment fragment = EditLangFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.edit_container, fragment, TAG_EDIT_FRAGMENT).commit();
        }
        else {
            Intent i = new Intent(this, EditLangActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onTableLangEvent(DataEventType eventType, Lang lang) {
        updateList();
        hideEdit();
    }
}