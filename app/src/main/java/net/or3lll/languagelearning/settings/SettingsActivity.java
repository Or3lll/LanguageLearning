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

import net.or3lll.languagelearning.R;
import net.or3lll.languagelearning.configuration.exporter.DataExporter;
import net.or3lll.languagelearning.configuration.importer.DataImporter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Paramètres");
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 42: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveData();
                }
                return;
            }

            case 69: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    restoreData();
                }
                return;
            }
        }
    }

    public void onSaveDataClick(View v) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    42);
        }
        else {
            saveData();
        }
    }

    private void saveData() {
        DataExporter exporter = new DataExporter();
        String jsonBackUp = exporter.export();

        if(isExternalStorageWritable()) {
            File backUpDir = getPublicStorageDir();
            File backUpFile = new File(backUpDir, BACK_UP_FILE_NAME);
            try {
                backUpFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fos = new FileOutputStream(backUpFile);
                fos.write(jsonBackUp.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRestoreDataClick(View v) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    69);
        }
        else {
            restoreData();
        }
    }

    private void restoreData() {
        if(isExternalStorageReadable()) {
            File backUpDir = getPublicStorageDir();
            File backUpFile = new File(backUpDir, BACK_UP_FILE_NAME);

            DataImporter importer = new DataImporter();
            try {
                importer.load(new BufferedReader(new FileReader(backUpFile)));
                importer.apply();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCleanDataClick(View v) {

    }

    public File getPublicStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory("BackUp"), BACK_UP_DIRECTORY_NAME);
        if (!file.mkdirs()) {
            Log.e("", "Echec création dir");
        }
        return file;
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
