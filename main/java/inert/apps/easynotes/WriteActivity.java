package inert.apps.easynotes;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;



public class WriteActivity extends AppCompatActivity {


    public EditText title;
    public EditText text;

    //для получения текста из EditText-ов
    String Title;
    String Text;


    //поля для сохранения насртоек размера текста
    public static final String SIZE_PREFERENCE = "sizeSettings";
    public static final String SIZE_PREFERENCE_KEY = "sizeKey";
    SharedPreferences sharedPreferences;

    //поле для определения размера текста и нажатаго RadioButton в диалоге
    public static int dialogChoiceId = 2;

    DB db;



    //метод для показа диалога для сохранения заметки
    public void ShowSaveDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        //текст сообщений
        alertDialog.setMessage(R.string.alert_message);
        //положительный ответ
        alertDialog.setPositiveButton(R.string.alert_yes,myClickListener);
        //отрицательный ответ
        alertDialog.setNegativeButton(R.string.alert_on, myClickListener);
        //нейтральный ответ
        alertDialog.setNeutralButton(R.string.alert_cancel, myClickListener);
        //метод вызова диалога
        alertDialog.show();

    }



    //метод для показа диалога для выбора размера текста
    public void ShowSizeDialog(){

        String[] AlertChoiceRes= {"Очень маленький", "Маленький", "Средний", "Большой", "Огромный"};

        AlertDialog.Builder alertChoice = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        alertChoice.setTitle(R.string.AlertChoiceTitle);
        alertChoice.setSingleChoiceItems(AlertChoiceRes, dialogChoiceId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0){
                    dialogChoiceId = 0;
                    title.setTextSize(16);
                    text.setTextSize(14.5f);
                }else if (which == 1){
                    dialogChoiceId = 1;
                    title.setTextSize(18);
                    text.setTextSize(16.5f);
                }else if (which == 2){
                    dialogChoiceId = 2;
                    title.setTextSize(20);
                    text.setTextSize(18.5f);
                }else if (which == 3){
                    dialogChoiceId = 3;
                    title.setTextSize(22);
                    text.setTextSize(20.5f);
                }else if (which == 4){
                    dialogChoiceId = 4;
                    title.setTextSize(24);
                    text.setTextSize(22.5f);
                }

            }
        });
        alertChoice.show();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);

        title = (EditText) findViewById(R.id.title);
        text = (EditText) findViewById(R.id.text);

        db = new DB(this);

       //создание файла и ключа с начальным значением 1
       sharedPreferences = getSharedPreferences(SIZE_PREFERENCE, MODE_PRIVATE);
       dialogChoiceId = sharedPreferences.getInt(SIZE_PREFERENCE_KEY, 2);

       //алгоритм для определения размера текста
        if (dialogChoiceId == 0){
            title.setTextSize(16);
            text.setTextSize(14.5f);

        }else if(dialogChoiceId == 1){
            title.setTextSize(18);
            text.setTextSize(16.5f);

        }else if (dialogChoiceId == 2){
            title.setTextSize(20);
            text.setTextSize(18.5f);

        }else if(dialogChoiceId == 3){
            title.setTextSize(22);
            text.setTextSize(20.5f);

        }else if (dialogChoiceId == 4){
            title.setTextSize(24);
            text.setTextSize(22.5f);
        }

    }




    ///////////////МЕНЮ\\\\\\\\\\\\

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Title = title.getText().toString();
        Text = text.getText().toString();


        if (id == R.id.save_item) {
            if (TextUtils.isEmpty(text.getText().toString()) && TextUtils.isEmpty(title.getText().toString())) {
                Toast.makeText(this, R.string.ToastText, Toast.LENGTH_SHORT).show();
            }else{
                //открываем подключение к БД
                db.Open();
                //добавляем значения из переменных
                db.Add(Title, Text);
                //закрываем подключение к базе данных
                db.Close();
                finish();
            }
        }


        if (id == R.id.cancel_item) {
            if (TextUtils.isEmpty(text.getText().toString()) && TextUtils.isEmpty(title.getText().toString())) {
                finish();
            }else{
                ShowSaveDialog();
            }
        }


        if(id == R.id.size_item){
           ShowSizeDialog();
        }

        return true;
    }




    //обработчик Диалога
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            Title = title.getText().toString();
            Text = text.getText().toString();

            switch (which){
                case Dialog.BUTTON_POSITIVE:
                    db.Open();
                    db.Add(Title, Text);
                    db.Close();
                    finish();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    finish();
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };





    //При нажатии назад выводит диалог если хотябы одно поле не пустое
    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(text.getText().toString()) && TextUtils.isEmpty(title.getText().toString())) {
            finish();
        }else{
            ShowSaveDialog();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //закрываем подключение к БД
        db.Close();

        //перед закрытием сохранять последнее занчение из dialogChoiceId в SIZE_PREFERENCE_KEY
        SharedPreferences.Editor sEditor = sharedPreferences.edit();
        sEditor.putInt(SIZE_PREFERENCE_KEY, dialogChoiceId);
        sEditor.apply();

    }




}