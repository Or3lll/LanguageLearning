package net.or3lll.languagelearning.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Or3lll on 29/11/2015.
 */
public class Word extends SugarRecord implements Parcelable {
    public Lang lang;
    public String text;
    public String subText;
    public String desc;

    public Word() {
    }

    public Word(Lang lang, String text, String subText, String desc) {
        this.lang = lang;
        this.text = text;
        this.subText = subText;
        this.desc = desc;
    }

    protected Word(Parcel in) {
        setId(in.readLong());
        lang = in.readParcelable(Lang.class.getClassLoader());
        text = in.readString();
        subText = in.readString();
        desc = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeParcelable(lang, flags);
        dest.writeString(text);
        dest.writeString(subText);
        dest.writeString(desc);
    }

    public static Word getWordByTextForLang(String text, Lang lang) {
        if(lang.getId() != null) {
            List<Word> words = Word.find(Word.class, "lang=? AND text=?", new String[]{lang.getId().toString(), text});
            if (words.size() == 1) {
                return words.get(0);
            }
        }

        return null;
    }
}
