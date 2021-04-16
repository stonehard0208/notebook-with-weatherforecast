package com.xstudioo.noteme;


import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;


public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle,noteDetails,mRemindTime; //文本标题，文本内容,提醒时间
    Calendar c;         // 日历对象
    String todaysDate;  // 日期
    String currentTime; // 当前时间
    private ClockManager mClockManager = ClockManager.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText tvRemindTime=findViewById(R.id.tv_remind_time_picker);
        tvRemindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickClick(v);
            }


        });





        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 给toolbar左边添加返回按钮
        getSupportActionBar().setTitle("创建新笔记");    // 设置标题

        noteDetails = findViewById(R.id.noteDetails);   // 内容文本
        noteTitle = findViewById(R.id.noteTitle);       // 标题文本
        mRemindTime = findViewById(R.id.tv_remind_time_picker);//提醒时间

        // 添加文本内容监听事件
        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            // 文本正在被改变时使用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 同步将已经输入在标题的内容展示在toolbar上
                Log.d("22222222222222", "onTextChanged: 123123123213123123213123213");
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 设置当前的日期和时间
        c = Calendar.getInstance(); //获取当前日期和时间点
        todaysDate = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: "+todaysDate);
        currentTime = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
        Log.d("TIME", "Time: "+currentTime);

    }

    // 如果小时数小于10在前面补0
    private String pad(int time) {
        if(time < 10)
            return "0"+time;
        return String.valueOf(time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 实例化menu的布局文件
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    // 目录选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EditText tvRemindTime=findViewById(R.id.tv_remind_time_picker);
        // 点击保存按钮
        Log.d("111111111111111", "onOptionsItemSelected: 1111111111111111111111111111111111111111111111111111111111111111");
        if(item.getItemId() == R.id.save){
            // 笔记标题不为空时才能保存
            Log.d("222", "onOptionsItemSelected:222222222222222222222222");
            if(noteTitle.getText().length() != 0) {
                Log.d("333", "onOptionsItemSelected:333333333333333333333333333333333333333333333333333333 ");
                // 将输入的内容和日期事件存进note对象
                Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime,mRemindTime.getText().toString());
               // mClockManager.addAlarm(buildIntent(note.getId()), DateTimeUtil.str2Date(note.getmRemindTime()));
                mClockManager.addAlarm(buildIntent(note.getId()), DateTimeUtil.str2Date(note.getmRemindTime()));
                // 向数据库插入数据
                SimpleDatabase sDB = new SimpleDatabase(this);
                int id = sDB.addNote(note);
                Note check = sDB.getNote(id);
                Log.d("inserted", "Note: " + id + " -> Title:" + check.getTitle() + " Date: " + check.getDate());

                onBackPressed();
                String time = tvRemindTime.getText().toString();
                Log.d("20182005274", time);

                // 显示信息已保存消息
                Toast.makeText(this, "笔记已保存~", Toast.LENGTH_SHORT).show();
            }
            else {
                noteTitle.setError("标题不能为空~");
            }
        }

        // 点击删除按钮取消保存
        else if(item.getItemId() == R.id.delete){
            Toast.makeText(this, "已取消~", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setEditTextReadOnly(EditText editText, boolean readOnly) {
        editText.setFocusable(!readOnly);
        editText.setFocusableInTouchMode(!readOnly);
        editText.setCursorVisible(!readOnly);

    }
    /**
     * 弹出时间选择器，选择闹钟执行时间
     * @param view
     */
    public void datePickClick(View view) {
        EditText tvRemindTime=findViewById(R.id.tv_remind_time_picker);
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNote.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = year + "-" + StringUtil.getLocalMonth(month) + "-" + StringUtil.getMultiNumber(dayOfMonth) + " " + StringUtil.getMultiNumber(hourOfDay) + ":" + StringUtil.getMultiNumber(minute);
                        Log.d("20182005274", time);
                        tvRemindTime.setText(time);
                        String changetime = tvRemindTime.getText().toString();
                        Log.d("20182005274", time);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    private PendingIntent buildIntent(int id) {
        Intent intent = new Intent("com.liuzhengwei.memo.action.CLOCK_RECEIVER");
        intent.putExtra(ClockReceiver.EXTRA_EVENT_ID, id);
        intent.setClass(this, ClockReceiver.class);

        return PendingIntent.getBroadcast(this, 0x001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
