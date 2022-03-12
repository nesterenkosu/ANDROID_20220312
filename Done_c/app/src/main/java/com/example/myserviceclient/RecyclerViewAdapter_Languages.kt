package com.example.myserviceclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

import com.example.myserviceclient.mydata.*

class RecyclerViewAdapter_Languages(
        private val rv_items: ArrayList<Language>,
        private val onClickListener: RecyclerViewOnClickListener
    ) : RecyclerView.Adapter<RecyclerViewAdapter_Languages.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Связывание текстовых полей, объявленных в файле разметки (recycler_item_Language.xml)
        //с переменными в данном коде
        val tv_name: TextView = view.findViewById<View>(R.id.tv_name) as TextView
    }

    //Подключение файла разметки (recycler_item_Language.xml)
    //к данному файлу с кодом
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_language, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Эта функция вызывается последовательно
        //для каждого элемента списка RecyclerView
        //holder - экземпляр объекта разметки, созданный для
        //отображения текущего элемента
        //position - порядковый номер текущего элемента

        //получение данных для текущего элемента списка
        //из переданного в конструктор массива
        val rw_item = rv_items[position]

        //вывод свойств полученного объекта данных
        //в соответствующие текстовые поля разметки
        holder.tv_name.text = rw_item.Name

        //назначение текущему элементу списка
        //обработчика для события Click
        holder.itemView.setOnClickListener {
            onClickListener.onClicked(rw_item.uid as Int)
        }
    }


    override fun getItemCount(): Int {
        //Получение количества элементов в списке
        return rv_items.size
    }
}


