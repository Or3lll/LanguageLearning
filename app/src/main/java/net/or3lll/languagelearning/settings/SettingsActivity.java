package net.or3lll.languagelearning.settings;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import net.or3lll.languagelearning.R;

public class SettingsActivity extends AppCompatActivity {
    private Fragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_settings);
        ab.setDisplayHomeAsUpEnabled(true);

        settingsFragment = getFragmentManager().findFragmentById(R.id.fragment_settings);
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        settingsFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
