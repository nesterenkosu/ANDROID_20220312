package com.example.myserviceclient

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class ActivityLanguage : AppCompatActivity() {
    private lateinit var ed_name: EditText
    private lateinit var ed_age: EditText
    private lateinit var ed_email: EditText
    private lateinit var btn_go: Button
    private lateinit var btn_delete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        //Задать заголовок окна
        supportActionBar!!.title ="Язык программирования"

        //Добавить кнопку "Стрелка обратно"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        ed_name=findViewById(R.id.ed_name)
        btn_go=findViewById(R.id.btn_go)
        btn_delete=findViewById(R.id.btn_delete)

        //Если режим редактирования
        if(intent.getIntExtra("is_edit",-1)==1) {
            //заполняем текстовые поля на основе данных,
            //принятых из родительского окна
            ed_name.setText(intent.getStringExtra("language_name"))
        }else {
            //Скрываем кнопку "Удалить", если режим создания
            btn_delete.visibility= View.INVISIBLE
        }

        btn_go.setOnClickListener {
            //Формирование ответа - возврат данных обратно в первое окно
            val resultIntent = Intent()

            resultIntent.putExtra("language_name",ed_name.getText().toString())

            //RESULT_OK в случае, если нажата кнопка Сохранить
            setResult(Activity.RESULT_OK,resultIntent)
            finish()
        }

        btn_delete.setOnClickListener {
            //Создание всплывающего окна
            //для подтверждения удаления
            val builder = AlertDialog.Builder(this)
            //задание текста предупреждения
            builder.setMessage("Вы действительно хотите удалить данный язык программирования?")
            //создание подтверждающей кнопки и задание действий
            //выполняемых при нажатии на неё
            builder.setPositiveButton("Да") { _,_ ->
                //Формирование ответа - возврат данных обратно в первое окно
                val resultIntent = Intent()
                //RESULT_FIRST_USER в случае, если нажата кнопка Удалить
                setResult(Activity.RESULT_FIRST_USER,resultIntent)
                finish()
            }
            //создание отрицающей кнопки, при нажатии на
            //которой не будет выполнено никаких действий
            builder.setNegativeButton("Нет") {_,_->}
            //отображение созданного всплывающего окна
            builder.show()
        }
    }

    //Обработчик нажатия на кнопку "Стрелка обратно"
    override fun onSupportNavigateUp(): Boolean {
        //По нажатии на кнопку "Стрелка обратно"
        //вернуться в предыдущее окно
        onBackPressed()
        return true
    }
}