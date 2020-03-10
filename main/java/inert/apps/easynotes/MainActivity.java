package inert.apps.easynotes;




import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;


public class MainActivity extends AppCompatActivity {



    public static SwipeMenuListView listView;
    DB db;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;


    public static long listItemId; //глобальная переменная для id listView




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //открываем подключение к Базе Данных
        db = new DB(this);
        db.Open();


        //получаем курсор
        cursor = db.getAllData();
        startManagingCursor(cursor);



        //формируем массивы данных для Адаптера
        String[] from = {DB.KEY_TITLE, DB.KEY_TITLE, DB.KEY_TEXT};
        int[] to = {R.id.oval_text, R.id.title_item, R.id.text_item};


        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);

        listView = (SwipeMenuListView) findViewById(R.id.listView);
        listView.setAdapter(simpleCursorAdapter);


        //Для показа пустой разметки без заметок
        View empty = getLayoutInflater().inflate(R.layout.list_empty_view, null, false);
        addContentView(empty, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        listView.setEmptyView(empty);



        //Обрабатывает нажатие на пункт списка
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listItemId = id;

                //это чтобы не нажимать на несколько элементов сразу
                listView.setEnabled(false);
                listView.setClickable(false);

                //открываем ReadActivity для получения результата
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(intent);

                db.Read(id);
            }
        });





        //Создание меню при свайпе влево
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem delete = new SwipeMenuItem(getBaseContext());

                delete.setWidth(250);
                delete.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                delete.setTitle(R.string.delete);
                delete.setTitleSize(13);
                delete.setTitleColor(Color.WHITE);
                delete.setIcon(R.drawable.ic_delete_24dp);
                menu.addMenuItem(delete);
            }
        };


        //установить listView меню при свайпе
        listView.setMenuCreator(creator);


        //обработчик меню при свайпе
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {


                        //находим id элемента по позиции и удаляем
                        db.Delete(listView.getItemIdAtPosition(position));
                        //обновляем курсор
                        cursor.requery();

                return false;
            }
        });



}



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //закрываем подключение к БД
        db.Close();
    }







    ///////////////МЕНЮ\\\\\\\\\\\\

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.create) {
            Intent intent = new Intent(MainActivity.this, WriteActivity.class);
            startActivity(intent);
        }


        return true;
    }






}