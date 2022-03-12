package com.example.myserviceclient

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.lang.Exception

import com.example.myserviceclient.mydata.AppDatabase
import com.example.myserviceclient.mydata.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActivityUsers : AppCompatActivity() {
    private lateinit var tv_test: TextView
    private lateinit var fab_add: FloatingActionButton
    private lateinit var rv_users: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter_Users
    private lateinit var rv_users_items: ArrayList<User>
    private lateinit var db:AppDatabase

    private lateinit var my_intent: Intent

    private var edit_id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        //Задать заголовок окна
        supportActionBar!!.title ="Пользователи"

        //Добавить кнопку "Стрелка обратно"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Создание объкета для доступа к окну "Пользователь"
        my_intent = Intent(this,ActivityUser::class.java)

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
        rv_users=findViewById(R.id.rv_users)

        //задание в качестве компоновочного контейнера для
        //элементов списка компонента LinearLayoutManager
        //с вертикальным расположением элементов
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_users.layoutManager = linearLayoutManager

        //Создание массива элементов, через который в RecyclerView
        //будут поступать элементы списка
        rv_users_items = ArrayList<User>()
        //Создание экземпляра объекта RecyclerViewAdapter_Users,
        //определяющего логику работа элемента RecyclerView
        adapter = RecyclerViewAdapter_Users(rv_users_items, object :
                                                RecyclerViewOnClickListener {
            //Обработчик щелчка на элементе списка
            override fun onClicked(idx: Int) {
                try {
                    //ОТКРЫТИЕ ОКНА "Пользователь" ДЛЯ
                    //РЕДАКТИРОВАНИЯ ДАННЫХ

                    //Определяем id пользователя
                    //выбранного в списке
                    val edit_id=idx
                    //Извлечение данных выбранного пользователя из БД
                    //по его id
                    val user = db.userDao().getUserById(edit_id)

                    //Передаём открываемому окну флаг is_edit=1
                    //чтобы это окно открылось в режиме "Редактирование"
                    my_intent.putExtra("is_edit",1)

                    //Передача в открываемое окно  данных
                    //редактируемого пользователя
                    my_intent.putExtra("user_name",user.Name);
                    my_intent.putExtra("user_age",user.Age.toString());
                    my_intent.putExtra("user_email",user.Email);
                    my_intent.putExtra("user_languageid",user.LanguageID);

                    //Собственно открытие окна
                    startActivityForResult(my_intent, 2)
                }catch (ex:Exception) {
                    //Если при открытии окна произошла ошибка -
                    //выведем её
                    tv_test.setText("Ошибка "+ex.message)
                }
            }
        })

        rv_users.adapter = adapter
        /*
        //Тестируем работу RecyclerView

        //добавляем элементы в массив rv_users_items
        rv_users_items.add(User(1,"Vasya",30,"vasya@mail.ru"))
        rv_users_items.add(User(2,"Kolya",25,"kolya@mail.ru"))
        rv_users_items.add(User(3,"Petya",44,"kolya@mail.ru"))
        //и сообщаем RecyclerView, что данные в массиве изменились
        //и необходимо выполнить перерисовку списка
        adapter.notifyDataSetChanged()*/

        RecyclerViewReloadData()
        /*tags.addAll(db.userDao().getAll())
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

            //db.userDao().deleteAll()

            //Создание двух новых пользователей
            db.userDao().insertAll(User(null,"Andrew",20,"andrew@mail.ru"))
            db.userDao().insertAll(User(null,"Bill",25,"bill@mail.ru"))

            //Получение списка пользователей из базы данных
            val userList = db.userDao().getAll()
            s+="Тест выборки списка пользователей\n================\n"
            userList.forEach {
                s+=it.uid.toString()+" "+it.Name+" "+it.Email+" "+it.Age+"\n"
            }

            //Получение id пользователей для тестирования операций
            //получения, обновления и удаления данных
            var andrew_id = userList.first().uid!!.toInt()
            var bill_id = userList.last().uid!!.toInt()

            //Получение данных одного пользователя по его id
            val user = db.userDao().getUserById(andrew_id)
            s+="\nТест выборки одного пользователя\n================\n"
            s+=user.uid.toString()+" "+user.Name+" "+user.Email+" "+user.Age+"\n"

            //Обновление пользователя
            db.userDao().update(User(andrew_id,"Andrew",30,"andrew@gmail.com"))

            //Удаление пользователя
            db.userDao().delete(User(bill_id,"",0,""))

            s+="\nТест обновления и удаления пользователей\n================\n"
            db.userDao().getAll().forEach {
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
                var name=data!!.getStringExtra("user_name")
                var age=data!!.getStringExtra("user_age")
                var email=data!!.getStringExtra("user_email")
                var languageid = data!!.getIntExtra("user_languageid",-1)
                //Добавление нового пользователя в БД
                db.userDao().insertAll(
                        User(null,  name, age.toString().toInt(), email,languageid))
            }catch (ex: Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }

        //Обновление
       if(requestCode==2 && resultCode==Activity.RESULT_OK) {
            try {
                //Получение данных, введённых в окно "Пользователь"
                var name=data!!.getStringExtra("user_name")
                var age=data!!.getStringExtra("user_age")
                var email=data!!.getStringExtra("user_email")
                var languageid = data!!.getIntExtra("user_languageid",-1)
                //Обновление в БД пользователя с id=edit_id
                db.userDao().update(
                        User(edit_id,  name, age.toString().toInt(), email,languageid))
            }catch (ex: Exception){
                tv_test.setText("Ошибка "+ex.message)
            }
        }

        //Удаление
        if(requestCode==2 && resultCode==Activity.RESULT_FIRST_USER) {
            try {
                //Удаление из БД пользователя с id=edit_id
                db.userDao().delete(
                        User(edit_id,  "",0,"",-1))
            }catch (ex: Exception){
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
            rv_users_items.clear()
            //Загрузка в список данных из БД
            rv_users_items.addAll(db.userDao().getAll())
            //Уведомление RecyclerView о необходимости
            //перерисовки
            adapter.notifyDataSetChanged()
        }catch (ex:Exception){
            //В случае ошибки выведем сообщение
            tv_test.setText("Ошибка "+ex.message)
        }
    }
}

