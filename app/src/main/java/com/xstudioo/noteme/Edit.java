package com.xstudioo.noteme;


import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText nTitle,nContent,mRemindTime;
    Calendar c;
    String todaysDate;
    String currentTime;
    private ClockManager mClockManager = ClockManager.getInstance();

    int nId;
    private List<Note> notes = new ArrayList<>();


    @BindView(R.id.tv_remind_time_picker)
    EditText tvRemindTime;

    // 编辑笔记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit); // 加载布局
        EditText tvRemindTime=findViewById(R.id.tv_remind_time_picker);
        tvRemindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickClick(v);
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 加上返回的图标
        getSupportActionBar().setHomeButtonEnabled(true);   // 使图标成为可操作项

        Intent i = getIntent();
        nId = i.getIntExtra("ID",0);
        SimpleDatabase db = new SimpleDatabase(this);
        Note note = db.getNote(nId);

        final String title = note.getTitle();
        String content = note.getContent(); // 获取笔记内容
        nTitle = findViewById(R.id.noteTitle);
        nContent = findViewById(R.id.noteDetails);
        mRemindTime =findViewById(R.id.tv_remind_time_picker);
        String lasttime= note.getmRemindTime();
        mRemindTime.setText(lasttime);


        // 文本内容监听事件
        nTitle.addTextChangedListener(new TextWatcher() {
            // 随编辑的标题是否改变显示toolbar的文字
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nTitle.setText(title);
        nContent.setText(content);

        // 设置当前日期和时间
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: "+todaysDate);
        currentTime = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));
        Log.d("TIME", "Time: "+currentTime);



    }

    // 小时数不足10在前面补上0
    private String pad(int time) {
        if(time < 10)
            return "0"+time;
        return String.valueOf(time);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    // 目录选择
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Note note = new Note(nId,nTitle.getText().toString(),nContent.getText().toString(),todaysDate,currentTime,mRemindTime.getText().toString());
        // 保存笔记，更新数据库
        if(item.getItemId() == R.id.save){
            Log.d("EDITED", "edited: before saving id -> " + note.getId());
            SimpleDatabase sDB = new SimpleDatabase(getApplicationContext());
            int id = sDB.editNote(note);
            Log.d("EDITED", "EDIT: id " + id);
            mClockManager.addAlarm(buildIntent(note.getId()), DateTimeUtil.str2Date(note.getmRemindTime()));
            goToMain();
            Toast.makeText(this, "笔记已编辑~", Toast.LENGTH_SHORT).show();

            notes=sDB.getAllNotes();
        }
        // 删除笔记
        else if(item.getItemId() == R.id.delete){
            Toast.makeText(this, "已取消~", Toast.LENGTH_SHORT).show();
            mClockManager.cancelAlarm(buildIntent(note.getId()));
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // 跳转界面
    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private PendingIntent buildIntent(int id) {
        Intent intent = new Intent();
        intent.putExtra(ClockReceiver.EXTRA_EVENT_ID, id);
        intent.setClass(this, ClockReceiver.class);

        return PendingIntent.getService(this, 0x001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(Edit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = year + "-" + StringUtil.getLocalMonth(month) + "-" + StringUtil.getMultiNumber(dayOfMonth) + " " + StringUtil.getMultiNumber(hourOfDay) + ":" + StringUtil.getMultiNumber(minute);
                        tvRemindTime.setText(time);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }


}
