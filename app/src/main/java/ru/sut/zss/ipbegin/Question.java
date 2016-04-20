package ru.sut.zss.ipbegin;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private String question;
    private String answer;

    public Question() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }

    public static final Parcelable.Creator<Question> CREATOR
            = new Parcelable.Creator<Question>() {

        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    private Question(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }



}
