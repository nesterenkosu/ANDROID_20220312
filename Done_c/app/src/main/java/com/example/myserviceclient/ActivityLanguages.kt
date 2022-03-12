package com.example.myserviceclient

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.lang.Exception

import com.example.myserviceclient.mydata.AppDatabase
import com.example.myserviceclient.mydata.Language
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityLanguages : AppCompatActivity() {
    private lateinit var tv_test: TextView
    private lateinit var fab_add: FloatingActionButton
    private lateinit var rv_Languages: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter_Languages
    private lateinit var rv_Languages_items: ArrayList<Language>
    private lateinit var db:AppDatabase

    private lateinit var my_intent: Intent

    private var edit_id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languages)

        //Задать заголовок окна
        supportActionBar!!.title ="Языки программирования"

        //Добавить кнопку "Стрелка обратно"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Создание объкета для доступа к окну "Пользователь"
        my_intent = Intent(this,ActivityLanguage::class.java)

        //Соединение с базой данных
        try {
            db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "my_database_new5"
            ).allowMainThreadQueries().build()
        }catch (ex:Exception) {
            tv_test.setText("Ошибка "+ex.message)
        }

        fab_add = findViewById(R.id.fab_add)

        fab_add.setOnClickListener {

            //putExtra - передача в открываемое (второе) окно некоторых данных
            my_intent.putExtra("is_edit",0)

            //Откроем окно для создания нового
            startActivityForResult(my_intent,1)
        }

        //Проверка работы слоя доступа к данным
        //получение доступа к текстовому полю на форме
        tv_test=findViewById(R.id.tv_test)

        //Первоначальная настройка RecyclerView
        //получение доступа к элементу RecyclerView, расположенному на форме
        rv_Languages=findViewById(R.id.rv_languages)

        //задание в качестве компоновочного контейнера для
        //элементов списка компонента LinearLayoutManager
        //с вертикальным расположением элементов
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_Languages.layoutManager = linearLayoutManager

        //Создание массива элементов, через который в RecyclerView
        //будут поступать элементы списка
        rv_Languages_items = ArrayList<Language>()
        //Создание экземпляра объекта RecyclerViewAdapter_Languages,
        //определяющего логику работа элемента RecyclerView
        adapter = RecyclerViewAdapter_Languages(rv_Languages_items, object :
                RecyclerViewOnClickListener {
            //Обработчик щелчка на элементе списка
            override fun onClicked(idx: Int) {
                try {
                    //ОТКРЫТИЕ ОКНА "Пользователь" ДЛЯ
                    //РЕДАКТИРОВАНИЯ ДАННЫХ

                    //Определяем id пользователя
                    //выбранного в списке
                    edit_id=idx
                    //Извлечение данных выбранного пользователя из БД
                    //по его id
                    val Language = db.languageDao().getLanguageById(edit_id)

                    //Передаём открываемому окну флаг is_edit=1
                    //чтобы это окно открылось в режиме "Редактирование"
                    my_intent.putExtra("is_edit",1)

                    //Передача в открываемое окно  данных
                    //редактируемого пользователя
                    my_intent.putExtra("language_name",Language.Name);

                    //Собственно открытие окна
                    startActivityForResult(my_intent, 2)
                }catch (ex:Exception) {
                    //Если при открытии окна произошла ошибка -
                    //выведем её
                    tv_test.setText("Ошибка "+ex.message)
                }
            }
        })

        rv_Languages.adapter = adapter
        /*
        //Тестируем работу RecyclerView

        //добавляем элементы в массив rv_Languages_items
        rv_Languages_items.add(Language(1,"Vasya",30,"vasya@mail.ru"))
        rv_Languages_items.add(Language(2,"Kolya",25,"kolya@mail.ru"))
        rv_Languages_items.add(Language(3,"Petya",44,"kolya@mail.ru"))
        //и сообщаем RecyclerView, что данные в массиве изменились
        //и необходимо выполнить перерисовку списка
        adapter.notifyDataSetChanged()*/

        RecyclerViewReloadData()
        /*tags.addAll(db.languageDao().getAll())
        adapter.notifyDataSetChanged()*/

        //Переменная для формирования сообщения,
        //выводимого в текстовом поле
        /*var s = ""

        try {
            //Установка соединения с базой данных
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "my_database2"
            ).allowMainThreadQueries().build()

            //db.languageDao().deleteAll()

            //Создание двух новых пользователей
            db.languageDao().insertAll(Language(null,"Andrew",20,"andrew@mail.ru"))
            db.languageDao().insertAll(Language(null,"Bill",25,"bill@mail.ru"))

            //Получение списка пользователей из базы данных
            val LanguageList = db.languageDao().getAll()
            s+="Тест выборки списка пользователей\n================\n"
            LanguageList.forEach {
                s+=it.uid.toString()+" "+it.Name+" "+it.Email+" "+it.Age+"\n"
            }

            //Получение id пользователей для тестирования операций
            //получения, обновления и удаления данных
            var andrew_id = LanguageList.first().uid!!.toInt()
            var bill_id = LanguageList.last().uid!!.toInt()

            //Получение данных одного пользователя по его id
            val Language = db.languageDao().getLanguageById(andrew_id)
            s+="\nТест выборки одного пользователя\n================\n"
            s+=Language.uid.toString()+" "+Language.Name+" "+Language.Email+" "+Language.Age+"\n"

            //Обновление пользователя
            db.languageDao().update(Language(andrew_id,"Andrew",30,"andrew@gmail.com"))

            //Удаление пользователя
            db.languageDao().delete(Language(bill_id,"",0,""))

            s+="\nТест обновления и удаления пользователей\n================\n"
            db.languageDao().getAll().forEach {
                s+=it.uid.toString()+" "+it.Name+" "+it.Email+" "+it.Age+"\n"
            }

           //tv_test.setText(s)
        }catch (ex: Exception) {
            tv_test.setText("Ошибка "+ex.message)
        }*/
    }

    //Обработчик нажатия на кнопку "Стрелка обратно"
    override fun onSupportNavigateUp(): Boolean {
        //По нажатии на кнопку "Стрелка обратно"
        //вернуться в предыдущее окно
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Обработчик завершения дочернего окна

        //Создание
        if(requestCode==1 && resultCode==Activity.RESULT_OK) {
            try {
                //Получение данных, введённых в окно "Пользователь"
                var name=data!!.getStringExtra("language_name")
                //Добавление нового пользователя в БД
                db.languageDao().insertAll(Language(null,  name))
            }catch (ex: Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }

        //Обновление
        if(requestCode==2 && resultCode==Activity.RESULT_OK) {
            try {
                //Получение данных, введённых в окно "Пользователь"
                var name=data!!.getStringExtra("language_name")

                //Обновление в БД пользователя с id=edit_id
                db.languageDao().update(Language(edit_id,  name))
            }catch (ex: Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }

        //Удаление
        if(requestCode==2 && resultCode==Activity.RESULT_FIRST_USER) {
            try {
                //Удаление из БД пользователя с id=edit_id
                db.languageDao().delete(Language(edit_id,  ""))
            }
            catch(ex:SQLiteConstraintException) {
                tv_test.setText("Ошибка. Нельзя удалить язык программирования, который назначен хотя бы одному пользователю ["+ex.toString()+"]");
            }
            catch (ex: Exception){

                tv_test.setText("Ошибка "+ex.message)
            }
        }

        //Обновление списка в окне "Пользователи"
        RecyclerViewReloadData()
    }

    fun RecyclerViewReloadData() {
        //Обновить данные в RecyclerView
        try {
            //Очистка списка элементов
            rv_Languages_items.clear()
            //Загрузка в список данных из БД
            rv_Languages_items.addAll(db.languageDao().getAll())
            //Уведомление RecyclerView о необходимости
            //перерисовки
            adapter.notifyDataSetChanged()
        }catch (ex:Exception){
            //В случае ошибки выведем сообщение
            tv_test.setText("Ошибка "+ex.message)
        }
    }
}

