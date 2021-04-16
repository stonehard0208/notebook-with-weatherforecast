package com.xstudioo.noteme;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * 笔记类
 * id:每个笔记的id
 * title:笔记的标题
 * content:笔记的内容
 * date:笔记创建/更新的日期
 * time:笔记创建/更新的时间*/
public class Note implements BaseColumns, Parcelable {
    private int id;
    private String title;
    private String content;
    //x年x月x日
    private String date;
    //x点
    private String time;
    //更新时间
    private String mUpdatedTime;
    //闹钟表示位：该事件是否已经响过铃了，默认没有
    private Integer mIsClocked = 0;
    //提醒时间
    private String mRemindTime;

    Note(String title,String content,String date, String time,String mRemindTime){
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.mRemindTime = mRemindTime;

    }

    Note(int id,String title,String content,String date, String time,String mRemindTime){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.mRemindTime = mRemindTime;
    }
    Note(){
       // empty constructor
    }

    protected Note(Parcel in) {

        id = in.readInt();
        title = in.readString();
        content = in.readString();
        date = in.readString();
        time = in.readString();
        mRemindTime = in.readString();

    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getmUpdatedTime() {
        return mUpdatedTime;
    }

    public void setmUpdatedTime(String mUpdatedTime) {
        this.mUpdatedTime = mUpdatedTime;
    }

    public Integer getmIsClocked() {
        return mIsClocked;
    }

    public void setmIsClocked(Integer mIsClocked) {
        this.mIsClocked = mIsClocked;
    }

    public String getmRemindTime() {
        return mRemindTime;
    }

    public void setmRemindTime(String mRemindTime) {
        this.mRemindTime = mRemindTime;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(mRemindTime);
    }
}
