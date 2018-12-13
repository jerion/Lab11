package com.example.user.lab11;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText ed_book, ed_price;
    private Button bt_query, bt_insert, bt_updata, bt_delete;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_book = findViewById(R.id.ed_book);
        ed_price = findViewById(R.id.ed_price);
        bt_delete = findViewById(R.id.bt_delete);
        bt_insert = findViewById(R.id.bt_insert);
        bt_query = findViewById(R.id.bt_query);
        bt_updata = findViewById(R.id.bt_updata);
        listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        database = new MyDBHelepr(this).getWritableDatabase();

        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;
                if (ed_book.length()<1)
                    cursor = database.rawQuery("SELECT * FROM myTable", null);
                else
                    cursor = database.rawQuery("SELECT * FROM myTable WHERE book LIKE '" + ed_book.getText().toString() + "'", null);
                cursor.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this, "共有" + cursor.getCount() + "筆資料", Toast.LENGTH_SHORT).show();
                for (int i = 0; i<cursor.getCount(); i++){
                    items.add("書名:" + cursor.getString(0) + "\t\t\t\t 價格:" + cursor.getString(1));
                    cursor.moveToNext();
                }
                adapter.notifyDataSetChanged();
                cursor.close();
            }
        });

        bt_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_book.length()<1 || ed_price.length()<1)
                    Toast.makeText(MainActivity.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        database.execSQL("insert into myTable(book, price) values(?, ?)",
                                new Object[]{ed_book.getText().toString(),
                                ed_price.getText().toString()});
                        Toast.makeText(MainActivity.this, "新增書名" + ed_book.getText().toString()
                                + "    價格" + ed_price.getText().toString(), Toast.LENGTH_SHORT).show();

                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "新增失敗" +
                        e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        bt_updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_book.length()<1 || ed_price.length()<1)
                    Toast.makeText(MainActivity.this,"欄位請勿留空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try{
                        database.execSQL("UPDATE myTable SET price = " +
                                ed_price.getText().toString() + " WHERE book LIKE '" +
                                ed_book.getText().toString() + "'");
                        Toast.makeText(MainActivity.this,
                                "更新書名"+ ed_book.getText().toString()
                                        +" 價格"+ ed_price.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"更新失敗:"+
                                e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_book.length()<1)
                    Toast.makeText(MainActivity.this,"書名請勿留空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try{
                        database.execSQL("delete from myTable where book like '"
                        + ed_book.getText().toString() + "'");
                        Toast.makeText(MainActivity.this, "刪除書名" +
                        ed_book.getText().toString(), Toast.LENGTH_SHORT).show();
                        ed_book.setText("");
                        ed_price.setText("");
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"刪除失敗:"+
                                e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();;
        database.close();
    }
}
