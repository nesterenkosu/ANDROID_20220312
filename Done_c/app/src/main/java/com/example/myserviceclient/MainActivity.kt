package com.example.myserviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var btn_users: Button
    private lateinit var btn_languages: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Задать заголовок окна
        supportActionBar!!.title ="Клиент веб-сервиса mysite.ru"


        btn_users=findViewById(R.id.btn_ShowActivityUsers)

        btn_users.setOnClickListener {
            //Открытие окна "Пользователи"
            val my_intent = Intent(this,ActivityUsers::class.java)
            startActivity(my_intent)
        }

        btn_languages=findViewById(R.id.btn_ShowActivityLanguages)

        btn_languages.setOnClickListener {
            //Открытие окна "Языки программирования"
            try {
                val my_intent = Intent(this, ActivityLanguages::class.java)
                startActivity(my_intent)
            }
            catch(ex:Exception) {
                supportActionBar!!.title=ex.toString()
            }
        }

        //Самостоятельно реализовать открытие окон "Синхронизация с сервером" и "Настройки"
        //...

    }
}

