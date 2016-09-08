package net.or3lll.languagelearning.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.exporter.DataExporter;
import net.or3lll.languagelearning.configuration.importer.DataImporter;
import net.or3lll.languagelearning.data.Lang;
import net.or3lll.languagelearning.data.Translation;
import net.or3lll.languagelearning.data.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SettingsActivity extends AppCompatActivity {

    private static final String BACK_UP_FILE_NAME = "backup_data.json";
    private static final String BACK_UP_DIRECTORY_NAME = "language_backup";

    public static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 0;
    public static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.title_activity_settings);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveData();
                }
                else {
                    Toast.makeText(this, R.string.error_no_permission_save, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    restoreData();
                }
                else {
                    Toast.makeText(this, R.string.error_no_permission_restore, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onSaveDataClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
        else {
            saveData();
        }
    }

    public void onRestoreDataClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }
        else {
            restoreData();
        }
    }

    public void onCleanDataClick(View v) {
        Translation.deleteAll(Translation.class);
        Word.deleteAll(Word.class);
        SugarRecord.deleteAll(Lang.class);
    }

    private void saveData() {
        if(isExternalStorageWritable()) {
            File backUpDir = new File(Environment.getExternalStoragePublicDirectory(BACK_UP_DIRECTORY_NAME), BACK_UP_DIRECTORY_NAME);
            File backUpFile = new File(backUpDir, BACK_UP_FILE_NAME);
            FileOutputStream fos = null;
            try {
                backUpDir.mkdirs();
                backUpFile.createNewFile();

                DataExporter exporter = new DataExporter();
                String jsonBackUp = exporter.export();

                fos = new FileOutputStream(backUpFile);
                fos.write(jsonBackUp.getBytes());
                fos.close();
            } catch (IOException e) {
                Toast.makeText(this, R.string.error_save_backup, Toast.LENGTH_SHORT).show();
            } finally {
                if(fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) { }
                }
            }
        }
        else {
            Toast.makeText(this, R.string.error_external_storage_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    private void restoreData() {
        if(isExternalStorageReadable()) {
            File backUpDir = new File(Environment.getExternalStoragePublicDirectory(BACK_UP_DIRECTORY_NAME), BACK_UP_DIRECTORY_NAME);
            File backUpFile = new File(backUpDir, BACK_UP_FILE_NAME);

            try {
                DataImporter importer = new DataImporter();
                importer.load(new BufferedReader(new FileReader(backUpFile)));
                importer.apply();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, R.string.error_restore_backup, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, R.string.error_external_storage_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
