package com.example.myrestclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Exception
import kotlinx.coroutines.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import kotlin.coroutines.CoroutineContext



class MainActivity : AppCompatActivity(),CoroutineScope {

    private val rootJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + rootJob

    private lateinit var tv_main: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_main=findViewById(R.id.tv_main)

        WorkWithSoap()
    }

    //SOAP
    public fun WorkWithSoap() = launch{

        MBNMyServiceBinding().CreateLanguage("Kotlin")
    }

    public suspend fun TestCreatePersonSoap(){
        //Формирование документа SOAP-запроса
        val request = SoapObject("http://localhost/","CreatePerson_Request")

        //передача аргументов для вызываемого метода
        request.addProperty("Name","SOAP TESTER")
        request.addProperty("Age",20)
        request.addProperty("Mail","soap@mail.ru")
        request.addProperty("LanguageID",8)

        val env = SoapSerializationEnvelope(SoapEnvelope.VER12)
        env.dotNet=true
        env.setOutputSoapObject(request)

        //Отправка сформированного документа
        withContext(Dispatchers.IO) {
            val t = HttpTransportSE("http://10.0.2.2:8888/soap.api.php")
            t.call("http://localhost/CreatePerson",env)

        }
    }

    public suspend fun TestListPeopleSoap(){
        //Формирование документа SOAP-запроса
        val request = SoapObject("http://localhost/","ListPeople_Request")

        val env = SoapSerializationEnvelope(SoapEnvelope.VER12)
        env.dotNet=true
        env.setOutputSoapObject(request)

        //Отправка сформированного документа
        var s = withContext(Dispatchers.IO) {
            val t = HttpTransportSE("http://10.0.2.2:8888/soap.api.php")
            t.call("http://localhost/CreatePerson",env)

            val response = env.bodyIn as SoapObject

            var s=""
            for(i in 0..response.propertyCount-1)
                s+=(response.getProperty(i) as SoapObject).getProperty("Name").toString()+"\n"

            s
        }

        tv_main.setText(s)
    }
    //REST
    //Объявление функции, в которой возможно создавать дополнительные потоки
    public fun WorkWithRest() = launch{

        TestListPeople()

    }

    public suspend fun TestCreatePerson(){
        try {
            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:8888/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            val service = retrofit.create(MyServiceAPI::class.java)


            //Выполнение действия в другом потоке
            withContext(Dispatchers.IO) {
                service.CreatePerson(REST_Person(0, "New Retrofit Tester", 20, "retro@mail.ru", 8)).execute()
            }

            tv_main.setText("Пользователь успешно создан")
        }catch (ex:Exception) {
            tv_main.setText("Ошибка: "+ex.toString())
        }
    }

    public suspend fun TestListPeople(){
        try {
            val retrofit = Retrofit.Builder().baseUrl("http://10.0.2.2:8888/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            val service = retrofit.create(MyServiceAPI::class.java)

            //Выполнение действия в другом потоке
            var s =withContext(Dispatchers.IO) {
                var s = ""
                service.ListPeople().execute().body()?.forEach {
                    s+=it.Name+" "+it.Mail+"\n"
                }

                s
            }

            tv_main.setText(s)
        }catch (ex:Exception) {
            tv_main.setText("Ошибка: "+ex.toString())
        }
    }
}