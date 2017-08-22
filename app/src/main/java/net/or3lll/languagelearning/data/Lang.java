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
        defaultLangs.add(new Lang("Français", "fr_FR",
                new String(Character.toChars(0x1F1EB)) + new String(Character.toChars(0x1F1F7)))); //TODO a mettre dans les ressources
        defaultLangs.add(new Lang("Anglais", "en_GB",
                new String(Character.toChars(0x1F1EC)) + new String(Character.toChars(0x1F1E7)))); //TODO a mettre dans les ressources
        defaultLangs.add(new Lang("Russe", "ru_RU",
                new String(Character.toChars(0x1F1F7)) + new String(Character.toChars(0x1F1FA)))); //TODO a mettre dans les ressources
        defaultLangs.add(new Lang("Japonais", "ja_JP",
                new String(Character.toChars(0x1F1EF)) + new String(Character.toChars(0x1F1F5)))); //TODO a mettre dans les ressources
    }

    public static String JSON_PARAM_GROUP_NAME = "langs";
    public static String JSON_PARAM_ISOCODE = "isoCode";
    public static String JSON_PARAM_NAME = "name";
    public static String JSON_PARAM_EMOJI_FLAG = "flag";

    private Long id;

    private String name;
    private String isoCode;
    private String emojiFlag;

    public Lang() {
        this.name = "";
        this.isoCode = "";
        this.emojiFlag = "";
    }

    public Lang(String name, String isoCode, String emojiFlag) {
        this.name = name;
        this.isoCode = isoCode;
        this.emojiFlag = emojiFlag;
    }

    protected Lang(Parcel in) {
        id = in.readLong();
        name = in.readString();
        isoCode = in.readString();
        emojiFlag = in.readString();
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
    public String getEmojiFlag() {
        return emojiFlag;
    }

    public void setEmojiFlag(String emojiFlag) {
        if(!this.emojiFlag.equals(emojiFlag)) {
            this.emojiFlag = emojiFlag;
            notifyPropertyChanged(BR.emojiFlag);
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
            notifyPropertyChanged(BR.validIsoCode);
        }
    }

    public Long getId() {
        return id;
    }

    @Bindable
    public boolean isValid() {
        return isValidName() && isValidIsoCode();
    }

    public boolean isValidName() {
        return (name != null && name.length() >= NAME_MIN_LENGTH);
    }

    @Bindable
    public boolean isValidIsoCode() {
        if (isoCode.matches("[a-z]{2}_[A-Z]{2}")) {

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
        dest.writeString(emojiFlag);
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
            json.put(JSON_PARAM_EMOJI_FLAG, emojiFlag);
            return json;
        }
        catch (JSONException e) { }

        return null;
    }

    public static Lang jsonImport(JSONObject json) {
        try {
            String isoCode = json.getString(JSON_PARAM_ISOCODE);
            String name = json.getString(JSON_PARAM_NAME);
            String emojiFlag = json.getString(JSON_PARAM_EMOJI_FLAG);

            Lang lang = new Lang(name, isoCode, emojiFlag);
            if (lang.isValid()) {
                return lang;
            }
        }
        catch (JSONException e) { }

        return null;
    }
}
