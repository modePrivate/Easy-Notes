package inert.apps.easynotes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DB {

    //Константы для Версии БД, Имени БД и имени таблицы
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "myDatabase";
    public static final String MY_TABLE = "myTable";

    // константы для заголовков столбцов таблицы
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TEXT = "text";

    public static String text_db = "";
    public static String title_db = "";

    private final Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase database;



    public DB(Context context) {
        this.context = context;
    }


    //открыть подключение
    public void Open(){
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
    }


    //закрыть подключение
    public void Close(){
        if (dbHelper != null)
            dbHelper.close();
    }


    //получить все записи из таблицы MY_TABLE
    public Cursor getAllData(){
        return database.query(MY_TABLE, null, null, null, null, null ,KEY_ID + " DESC");
    }



    //добавление записи в таблицу MY_TABLE
    public void Add(String title, String text){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_TEXT, text);
        database.insert(MY_TABLE, null, contentValues);
    }



    //Удаление из таблицы по id
    public void Delete(long id){
        database.delete(MY_TABLE,KEY_ID +" = "+ id, null);
    }



    //Чтение из Базы Данных
    public void Read(long id){
         Cursor cursor = database.query(MY_TABLE, null,KEY_ID + "= ?", new String[]{String.valueOf(id)}, null,null,null);
        if (cursor.moveToFirst()) {
          title_db = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
          text_db = cursor.getString(cursor.getColumnIndex(KEY_TEXT));
        }
        cursor.close();
    }



    //обновление записи
    public void Update(long id, String updTitle, String updText){
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_TITLE, updTitle);
            contentValues.put(KEY_TEXT, updText);
            database.update(MY_TABLE, contentValues, KEY_ID + "= ?", new String[]{String.valueOf(id)});
    }






    //класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        //Вызывается при первм создании БД
        @Override
        public void onCreate(SQLiteDatabase db) {

            //метод для создания БД для выполненя SQL запроса который создает таблицу
            db.execSQL("create table " + MY_TABLE + "(" + KEY_ID + " integer primary key," + KEY_TITLE + " text," + KEY_TEXT + " text" + ")");

        }


        //Выывается при изменении БД
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // запрос на уничтожения таблицы,
            db.execSQL("drop table if exists " + MY_TABLE);

            // после которой будет вызываться метод onCreate с обновленной структурой
            onCreate(db);
        }
    }


}

