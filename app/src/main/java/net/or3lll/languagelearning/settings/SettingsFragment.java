package net.or3lll.languagelearning.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

/**
 * Created by or3lll on 11/09/2016.
 */
public class SettingsFragment extends PreferenceFragment {
    private static final String BACK_UP_FILE_NAME = "backup_data.json";
    private static final String BACK_UP_DIRECTORY_NAME = "language_backup";

    public static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 0;
    public static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    private Preference saveBackupPref;
    private Preference restoreBackupPref;
    private Preference clearBackupPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        Preference.OnPreferenceClickListener clickListener  = preference -> {
            if(preference == saveBackupPref) {
                saveDataWithCheckPermission();
                return true;
            }
            else if(preference == restoreBackupPref) {
                restoreDataWithCheckPermission();
                return true;
            }
            else if(preference == clearBackupPref) {
                clearDataClickWithCheckPermission();
                return true;
            }

            return false;
        };

        saveBackupPref = findPreference("pref_backup_save");
        restoreBackupPref = findPreference("pref_backup_restore");
        clearBackupPref = findPreference("pref_backup_clear");

        saveBackupPref.setOnPreferenceClickListener(clickListener);
        restoreBackupPref.setOnPreferenceClickListener(clickListener);
        clearBackupPref.setOnPreferenceClickListener(clickListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveData();
                }
                else {
                    Toast.makeText(getActivity(), R.string.error_no_permission_save, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    restoreData();
                }
                else {
                    Toast.makeText(getActivity(), R.string.error_no_permission_restore, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void saveDataWithCheckPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
        else {
            saveData();
        }
    }

    public void restoreDataWithCheckPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }
        else {
            restoreData();
        }
    }

    public void clearDataClickWithCheckPermission() {
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
                Toast.makeText(getActivity(), R.string.error_save_backup, Toast.LENGTH_SHORT).show();
            } finally {
                if(fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) { }
                }
            }
        }
        else {
            Toast.makeText(getActivity(), R.string.error_external_storage_unavailable, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), R.string.error_restore_backup, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity(), R.string.error_external_storage_unavailable, Toast.LENGTH_SHORT).show();
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
