package net.or3lll.languagelearning.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Or3lll on 29/11/2015.
 */
public class Translation extends SugarRecord implements Parcelable {
    public static String JSON_PARAM_GROUP_NAME = "translations";
    public static String JSON_PARAM_ISOCODE1 = "isoCode1";
    public static String JSON_PARAM_TEXT1 = "text1";
    public static String JSON_PARAM_ISOCODE2 = "isoCode2";
    public static String JSON_PARAM_TEXT2 = "text2";

    public Word word1;
    public Word word2;

    public Translation() {
    }

    public Translation(Word word1, Word word2) {
        this.word1 = word1;
        this.word2 = word2;
    }

    protected Translation(Parcel in) {
        setId(in.readLong());
        word1 = in.readParcelable(Word.class.getClassLoader());
        word2 = in.readParcelable(Word.class.getClassLoader());
    }

    public static final Creator<Translation> CREATOR = new Creator<Translation>() {
        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeParcelable(word1, flags);
        dest.writeParcelable(word2, flags);
    }

    public JSONObject jsonExport() {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_PARAM_ISOCODE1, word1.lang.getIsoCode());
            json.put(JSON_PARAM_TEXT1, word1.text);
            json.put(JSON_PARAM_ISOCODE2, word2.lang.getIsoCode());
            json.put(JSON_PARAM_TEXT2, word2.text);
            return json;
        }
        catch (JSONException e) { }

        return null;
    }
}
