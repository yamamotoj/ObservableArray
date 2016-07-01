package com.github.yamamotoj.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.yamamotoj.observablearray.ObservableArrayList
import com.observablearray.plus

class MainActivity : AppCompatActivity() {

    val arrayList1 = ObservableArrayList<String>()
    val arrayList2 = ObservableArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arrayList1.add("0")
        arrayList1.add("1")
        arrayList1.add("2")
        arrayList1.add("3")

        arrayList2.add("AA")
        arrayList2.add("BB")
        arrayList2.add("BB")
        arrayList2.add("DD")

        val arrayList = arrayList1 + arrayList2
        val adapter = ListAdapter(arrayList, object : ListAdapter.OnClickListener {
            override fun onClick(string: String) {
                if (string.length == 1) {
                    arrayList1.remove(string)
                } else {
                    arrayList2.remove(string)
                }
            }
        })

        setContentView(R.layout.activity_main)
        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
