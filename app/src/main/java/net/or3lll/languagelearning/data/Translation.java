package net.or3lll.languagelearning.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Or3lll on 29/11/2015.
 */
public class Translation extends SugarRecord implements Parcelable {
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
}
