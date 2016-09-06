package net.or3lll.languagelearning.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import net.or3lll.languagelearning.BR;
import net.or3lll.languagelearning.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Or3lll on 29/11/2015.
 */
@Table
public class Lang extends BaseObservable implements Parcelable {

    private static final int NAME_MIN_LENGTH = 3;

    // TODO voir si c'est pertinant de stocker comme ça
    public static ArrayList<Lang> defaultLangs = new ArrayList<>();
    static {
        defaultLangs.add(new Lang("Français", "fr_FR")); //TODO a mettre dans les ressources
        defaultLangs.add(new Lang("Anglais", "en_GB")); //TODO a mettre dans les ressources
        defaultLangs.add(new Lang("Russe", "ru_RU")); //TODO a mettre dans les ressources
        defaultLangs.add(new Lang("Japonais", "jn_JP")); //TODO a mettre dans les ressources
    }

    // TODO voir si c'est pertinant de stocker comme ça
    public static ArrayMap<String, Integer> flags = new ArrayMap<>();
    static {
        flags.put("fr_FR", R.drawable.french_flag);
        flags.put("en_GB", R.drawable.uk_flag);
        flags.put("ru_RU", R.drawable.russian_flag);
        flags.put("jn_JP", R.drawable.japan_flag);
    }

    private static String JSON_PARAM_ISOCODE = "isoCode";
    private static String JSON_PARAM_NAME = "name";

    private Long id;

    private String name;
    private String isoCode;

    public Lang() {
        this.name = "";
        this.isoCode = "";
    }

    public Lang(String name, String isoCode) {
        this.name = name;
        this.isoCode = isoCode;
    }

    protected Lang(Parcel in) {
        id = in.readLong();
        name = in.readString();
        isoCode = in.readString();
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(!this.name.equals(name)) {
            this.name = name;
            notifyPropertyChanged(BR.name);
            notifyPropertyChanged(BR.valid);
        }
    }

    @Bindable
    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        if(!this.isoCode.equals(isoCode)) {
            this.isoCode = isoCode;
            notifyPropertyChanged(BR.isoCode);
            notifyPropertyChanged(BR.valid);
        }
    }

    public Long getId() {
        return id;
    }

    @Bindable
    public boolean isValid() {
        if(name != null && name.length() >= NAME_MIN_LENGTH
            && isoCode.matches("[a-z]{2}_[A-Z]{2}")) {

            List<Lang> langs = SugarRecord.find(Lang.class, "(name = ? or iso_Code = ?) and id != ?",
                    name, isoCode, (getId() != null ? getId().toString() : "-1"));

            return langs.size() == 0;
        }

        return false;
    }

    public static final Creator<Lang> CREATOR = new Creator<Lang>() {
        @Override
        public Lang createFromParcel(Parcel in) {
            return new Lang(in);
        }

        @Override
        public Lang[] newArray(int size) {
            return new Lang[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(isoCode);
    }

    public static Lang getLangByIsoCode(String isoCode) {
        List<Lang> langs = SugarRecord.find(Lang.class, "iso_code=?", isoCode);
        if(langs.size() == 1) {
            return langs.get(0);
        }

        return null;
    }

    public JSONObject jsonExport() {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_PARAM_ISOCODE, isoCode);
            json.put(JSON_PARAM_NAME, name);
            return json;
        }
        catch (JSONException e) { }

        return null;
    }
}
