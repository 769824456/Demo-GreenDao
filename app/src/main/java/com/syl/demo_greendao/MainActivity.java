package com.syl.demo_greendao;


import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import com.syl.dao.DaoMaster;
import com.syl.dao.DaoSession;
import com.syl.dao.User;
import com.syl.dao.UserDao;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/*
 * PACKAGE_NAME :com.syl.demo_greendao
 * VERSION :[V 1.0.0]
 * AUTHOR :  yulongsun
 * CREATE AT : 7/30/2015 11:07 AM
 * COPYRIGHT : InSigma HengTian Software Ltd.
 * E-MAIL: yulongsun@hengtiansoft.com
 * NOTE : 教程地址：http://www.open-open.com/lib/view/open1438065400878.html
 * 需要编译：compile 'de.greenrobot:greendao:1.3.7'
 */
public class MainActivity extends ListActivity {
    private SQLiteDatabase db;
    private EditText etName;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private Cursor cursor;
    public static final String TAG = "MainActivity";
    public static final String DB_NAME = "demo-greendao.db";
    private EditText etMobile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1.初始化数据库
        initDB();
        // 2.获取 UserDao 对象
        getUserDao();

        //3.
        String textColumn = UserDao.Properties.Name.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(getUserDao().getTablename(), getUserDao().getAllColumns(), null, null, null, null, orderBy);
        String[] from = {textColumn, UserDao.Properties.Mobile.columnName};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from,
                to);
        setListAdapter(adapter);
        //4.
        findViews();
    }

    private void findViews() {
        etName = (EditText) findViewById(R.id.et_name);
        etMobile = (EditText)findViewById(R.id.et_mobile);
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * 获得数据库操作对象
     *
     * @return
     */
    private UserDao getUserDao() {
        return daoSession.getUserDao();
    }

    /**
     * Button 点击的监听事件
     *
     * @param view
     */
    public void onMyButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_user:
                addUser();
                break;
            case R.id.btn_Search_user:
                searchUser();
                break;
        }
    }

    /**
     * 添加用户
     */
    private void addUser() {

        //1.
        String nameStr = etName.getText().toString();
        etName.setText("");
        String mobileStr = etMobile.getResources().toString();
        etMobile.setText("");

        //2.数据合法性校验...

        // 3.插入操作，简单到只要你创建一个 Java 对象
        User user = new User(null, nameStr, mobileStr, new Date());
        long resultCode = getUserDao().insert(user);
        Log.d(TAG, "Inserted new note, ID: " + user.getId());
        cursor.requery();
    }

    /**
     * 查找用户
     */
    private void searchUser() {
        // Query 类代表了一个可以被重复执行的查询
        Query query = getUserDao().queryBuilder()
//                .where(UserDao.Properties.Name.eq("zhangsan"))
                .orderAsc(UserDao.Properties.Id)
                .build();

//      查询结果以 List 返回
//      List<User> user = query.list();
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

}
