package com.xstudioo.noteme;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Detail extends AppCompatActivity {
    int id;
    private ClockManager mClockManager = new ClockManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 查看笔记内容时显示上方toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 根据笔记的id获取笔记的详情
        Intent i = getIntent();
        id = i.getIntExtra("ID",0);
        SimpleDatabase db = new SimpleDatabase(this);
        final Note note = db.getNote(id); // 通过笔记的id查询
        getSupportActionBar().setTitle(note.getTitle());    // 将toolbar的文字设置问笔记的标题
        TextView details = findViewById(R.id.noteDesc); // 文本设置为笔记的内容
        Log.d("20182005274" ,note.getContent());
        details.setText(note.getContent());
        Log.d("20182005274", (String) details.getText());
        details.setMovementMethod(new ScrollingMovementMethod());   // 设置文本内容的滑动

        // 删除按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDatabase db = new SimpleDatabase(getApplicationContext());
                db.deleteNote(id);  //删除笔记
                Toast.makeText(getApplicationContext(),"笔记已被删除~",Toast.LENGTH_SHORT).show();


                goToMain();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 如果点击的是右上方的编辑按钮
        if(item.getItemId() == R.id.edit){
            Intent i = new Intent(this,Edit.class);
            i.putExtra("ID",id);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // 跳转到笔记首页
    private void goToMain() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

    }
    private PendingIntent buildIntent(int id) {
        Intent intent = new Intent();
        intent.putExtra(ClockReceiver.EXTRA_EVENT_ID, id);
        intent.setClass(this, ClockReceiver.class);

        return PendingIntent.getService(this, 0x001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
