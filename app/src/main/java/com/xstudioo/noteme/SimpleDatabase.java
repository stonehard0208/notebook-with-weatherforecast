package com.xstudioo.noteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * */
public class SimpleDatabase extends SQLiteOpenHelper {
    // 规定数据库相关信息
    private static final int DATABASE_VERSION = 2;  // 版本
    private static final String DATABASE_NAME = "SimpleDB"; // 数据库名
    private static final String TABLE_NAME = "SimpleTable"; // 表名
    private static SimpleDatabase simpleDatabase = new SimpleDatabase();
    public SimpleDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    public SimpleDatabase(){
        super(NoteApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION );
    }
    public static SimpleDatabase getInstance() {
        return simpleDatabase;
    }
    // 规定表中每一列的列名
    private static final String KEY_ID = "id";  // 每个笔记的id号
    private static final String KEY_TITLE = "title";    // 每个笔记的标题
    private static final String KEY_CONTENT = "content";    // 每个笔记的内容
    private static final String KEY_DATE = "date";  // 日期
    private static final String KEY_TIME = "time";  // 时间
    private  static final String KEY_REMIND = "remind_time"; //提醒时间




    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
         String createDb = "CREATE TABLE "+TABLE_NAME+" ("+
                 KEY_ID+" INTEGER PRIMARY KEY,"+
                 KEY_TITLE+" TEXT,"+
                 KEY_CONTENT+" TEXT,"+
                 KEY_DATE+" TEXT,"+
                 KEY_TIME+" TEXT,"+
                 KEY_REMIND+" TEXT"
                + " )";
         db.execSQL(createDb);
    }

    // 如果存在更新版本的就需要更新
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    // 添加笔记，存储到数据库中
    public int addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE,note.getTitle());   // 往数据库插入数据用contentvalues.put(key,value)
        v.put(KEY_CONTENT,note.getContent());
        v.put(KEY_DATE,note.getDate());
        v.put(KEY_TIME,note.getTime());
        v.put(KEY_REMIND,note.getmRemindTime());

        // 添加到数据库中，如果成功就会返回记录的id
        int ID = (int) db.insert(TABLE_NAME,null,v);
        return  ID;
    }

    // 通过id号获取笔记
    public Note getNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID,KEY_TITLE,KEY_CONTENT,KEY_DATE,KEY_TIME,KEY_REMIND};// 列名
        // 获取的cursor是每一行的集合
        Cursor cursor=  db.query(TABLE_NAME,query,KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        // 如果查询结果不为空就跳转到第一行
        if(cursor != null && cursor.moveToNext())
            cursor.moveToFirst();

        if(cursor.getCount()==0){
            return new Note();
        }
        else{
            // 将结果返回
            return new Note(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
        }

    }

    // 获取所有笔记
    public List<Note> getAllNotes(){
        List<Note> allNotes = new ArrayList<>();    // 定义存储笔记note的列表用于返回查询结果
        String query = "SELECT * FROM " + TABLE_NAME+" ORDER BY "+KEY_ID+" DESC";// 选择所有内容
        SQLiteDatabase db = this.getReadableDatabase(); // 以只读方式打开数据库
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){   // 如果cursor不为空就执行，否则范围空列表
            do{
                Note note = new Note();
                // 用查询到的结果设置Note对象的各个属性
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                note.setmRemindTime(cursor.getString(5));
                // 将Note添加到列表中
                allNotes.add(note);
            }while (cursor.moveToNext());   // 下一个查询结果不为空时继续执行
        }
        return allNotes;
    }

    //将编辑好的笔记通过Note对象传进来，执行数据库更新操作
    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "Edited Title: -> "+ note.getTitle() + "\n ID -> "+note.getId());
        //获取Note的各个值
        c.put(KEY_TITLE,note.getTitle());
        c.put(KEY_CONTENT,note.getContent());
        c.put(KEY_DATE,note.getDate());
        c.put(KEY_TIME,note.getTime());
        c.put(KEY_REMIND,note.getmRemindTime());
        //数据库更新
        return db.update(TABLE_NAME,c,KEY_ID+"=?",new String[]{String.valueOf(note.getId())});
    }

    // 根据笔记的id执行删除
    void deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }

}
