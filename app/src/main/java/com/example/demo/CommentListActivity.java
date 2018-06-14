package com.example.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;
import com.example.demo.adapter.MyAdapter;
import com.example.demo.model.Comment;
import com.example.demo.model.MyTask;
import com.example.demo.model.User;

import java.util.ArrayList;


/**
 * Created by xiaowei on 2018/6/13.
 */

public class CommentListActivity extends AppCompatActivity {
    private int newsID;
    private ListView commentListView;
    private MyAdapter<Comment> myAdapter = null;
    private ArrayList<Comment> mData = null;
    private MyTask<ArrayList<Comment>> commentTask = null;

    public MyAdapter<Comment> getAdapter(){
        return myAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(this, Color.parseColor("#303F9F"));
        setContentView(R.layout.activity_commentlist);

        Intent intent = getIntent();
        newsID = intent.getIntExtra("newsID",-1);
       // initCommentList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
            actionBar.setTitle("Comments");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton fbtn_addComment = (FloatingActionButton) findViewById(R.id.add_comment);
        fbtn_addComment.setOnClickListener((v)-> {
                for (Comment cmt : mData){
                    if (cmt.getUserID() == User.getInstance().getUserID())
                    {
                        Toast.makeText(CommentListActivity.this,"您已评论",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
               Intent commentIntent= new Intent(CommentListActivity.this, CommentActivity.class);
               commentIntent.putExtra("newsID",newsID);
               startActivity(commentIntent);
        });
    }

    private void initCommentList() {
        mData = new ArrayList<>();
        commentListView = (ListView) findViewById(R.id.comment_list);
        myAdapter = new MyAdapter<Comment>(mData,R.layout.item_comment) {
            @Override
            public void bindView(ViewHolder holder, Comment obj) {
                holder.setText(R.id.item_comment_user, obj.getUsername());
                holder.setText(R.id.item_comment_content, obj.getContent());
            }
        };
        //ListView设置下Adapter：
        commentListView.setAdapter(myAdapter);
            ArrayList<Object> params = new ArrayList<>();
            params.add(new Integer(newsID));
            commentTask = new MyTask<>("getCommentList",params);
            commentTask.setCallBack(commentTask.new CallBack() {
                @Override
                public void setSomeThing(ArrayList<Comment> commentList) {
                    CommentListActivity.this.mData = commentList;
                    for (Comment cmt : mData){
                        myAdapter.add(cmt);
                    }
                    Log.i("CommentList",mData.toString());
                }
            });
            commentTask.execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getDelegate().onStart();
        initCommentList();
    }

    static void setStatusBarColor(AppCompatActivity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
}