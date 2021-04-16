package com.xstudioo.noteme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    TextView noItemText;
    SimpleDatabase simpleDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);   // 主界面上方的toolbar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));  // 设置toorbar颜色
        setSupportActionBar(toolbar);   // 将toolbar的实例传入

        noItemText = findViewById(R.id.noItemText); //用于显示是否有笔记

        simpleDatabase = new SimpleDatabase(this);  // 实例化一个simpleDatabase用于执行数据库操作
        List<Note> allNotes = simpleDatabase.getAllNotes(); // 获取所有笔记

        recyclerView = findViewById(R.id.allNotesList);
        if(allNotes.isEmpty()){     // 如果还没有笔记，就将显示"还未记过笔记"的界面设置为可见
            noItemText.setVisibility(View.VISIBLE);
        }else {                     // 已经做过笔记，则不能看见
            noItemText.setVisibility(View.GONE);
            displayList(allNotes);  // 展示笔记的滚动控件
        }

    }

    private void displayList(List<Note> allNotes) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,allNotes);   // 借助适配器传递数据
        recyclerView.setAdapter(adapter);
    }

    //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();  // 实例化menu的布局文件
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    // toolbar右边按钮点击时跳转
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add){
            Toast.makeText(this, "今天你想记录什么？", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,AddNote.class);
            startActivity(i);   // 跳转到添加笔记界面
        }
        return super.onOptionsItemSelected(item);
    }

    //活动处于运行状态时
    @Override
    protected void onResume() {
        super.onResume();
        List<Note> getAllNotes = simpleDatabase.getAllNotes();
        if(getAllNotes.isEmpty()){
            noItemText.setVisibility(View.VISIBLE);
        }else {
            noItemText.setVisibility(View.GONE);
            displayList(getAllNotes);
        }


    }

}
