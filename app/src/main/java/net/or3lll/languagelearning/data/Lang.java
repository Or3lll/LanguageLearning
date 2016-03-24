package net.or3lll.languagelearning.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

import com.orm.SugarRecord;

import net.or3lll.languagelearning.R;

import java.util.ArrayList;

/**
 * Created by Or3lll on 29/11/2015.
 */
public class Lang extends SugarRecord implements Parcelable {

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

    public String name;
    public String isoCode;

    public Lang() {
    }

    public Lang(String name, String isoCode) {
        this.name = name;
        this.isoCode = isoCode;
    }

    protected Lang(Parcel in) {
        setId(in.readLong());
        name = in.readString();
        isoCode = in.readString();
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
        dest.writeLong(getId());
        dest.writeString(name);
        dest.writeString(isoCode);
    }
}
