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


public class ReadActivity extends AppCompatActivity {

    public EditText title;
    public EditText text;
    DB db;


    //поле для определения размера текста и нажатаго RadioButton в диалоге
    public static int dialogChoiceId = 2;

    SharedPreferences sharedPreferences;



    //Метод для обновления записи
    public void IntentUpdate(){
            db.Open();
            db.Update(MainActivity.listItemId, title.getText().toString(), text.getText().toString());
            db.Close();
            //включения доступности listView после нажатия
            MainActivity.listView.setEnabled(true);
            MainActivity.listView.setClickable(true);
            finish();
    }


    //метод для показа диалога при изменении текста когда нажимается кнопка Закрыть(Х) либо назад
    public void ShowSaveChangesDialog(){

        String forChangeTitle = DB.title_db;
        String forChangeText = DB.text_db;

        if (forChangeTitle.equals(title.getText().toString()) && forChangeText.equals(text.getText().toString())) {
            finish();
        }else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            alertDialog.setMessage("Сохранить изменения?");
            alertDialog.setPositiveButton(R.string.alert_yes, myClickListener);
            alertDialog.setNegativeButton(R.string.alert_on, myClickListener);
            alertDialog.setNeutralButton(R.string.alert_cancel, myClickListener);
            alertDialog.show();
        }
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
        setContentView(R.layout.read);

        title = (EditText) findViewById(R.id.read_title);
        text = (EditText) findViewById(R.id.read_text);

        db = new DB(this);


        title.setText(DB.title_db);
        text.setText(DB.text_db);




        //чтение значения из файла и присвоение его dialogChoiceId
        sharedPreferences = getSharedPreferences(WriteActivity.SIZE_PREFERENCE, MODE_PRIVATE);
        dialogChoiceId = sharedPreferences.getInt(WriteActivity.SIZE_PREFERENCE_KEY, 2);

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




    //////////////МЕНЮ\\\\\\\\\\\\

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.updSave){
            if (TextUtils.isEmpty(text.getText().toString()) && TextUtils.isEmpty(title.getText().toString())) {
                Toast.makeText(this, R.string.ToastText, Toast.LENGTH_SHORT).show();
            }else{
                IntentUpdate();
            }
        }



        if (id == R.id.updClose){

            ShowSaveChangesDialog();
        }


        if (id == R.id.updSize){
            ShowSizeDialog();
        }

        return true;
    }



        @Override
    public void onBackPressed() {
         if (TextUtils.isEmpty(text.getText().toString()) && TextUtils.isEmpty(title.getText().toString())) {
             Toast.makeText(this, R.string.ToastText, Toast.LENGTH_SHORT).show();
         }else {
             ShowSaveChangesDialog();
         }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.Close();
        //включения доступности listView после нажатия
        MainActivity.listView.setEnabled(true);
        MainActivity.listView.setClickable(true);

        //перед закрытием сохранять последнее занчение из dialogChoiceId в SIZE_PREFERENCE_KEY
        SharedPreferences.Editor sEditor = sharedPreferences.edit();
        sEditor.putInt(WriteActivity.SIZE_PREFERENCE_KEY, dialogChoiceId);
        sEditor.apply();

    }


    //обработчик Диалога
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){
                case Dialog.BUTTON_POSITIVE:
                    IntentUpdate();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    finish();
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };



}
