package com.herringbone.quoteproject

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.herringbone.quoteproject.adapter.CustomAdapter
import kotlinx.android.synthetic.main.activity_quote.*
import com.herringbone.quoteproject.network.GetDataService
import com.herringbone.quoteproject.network.RetrofitClientInstance
import com.herringbone.quoteproject.model.RetroPhoto
import android.widget.Toast

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class QuoteActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                listView = findViewById(R.id.recipe_list_view)
// 1
                val recipeList = Recipe.getRecipesFromFile("recipes.json", this)
// 2
                val listItems = arrayOfNulls<String>(recipeList.size)
// 3
                for (i in 0 until recipeList.size) {
                    val recipe = recipeList[i]
                    listItems[i] = recipe.title
                }
// 4
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
                listView.adapter = adapter
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                listView = findViewById(R.id.recipe_list_view)
                val listItems = arrayOfNulls<String>(0)
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
                listView.adapter = adapter
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        /*Create handle for the RetrofitInstance interface*/
        val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.allPhotos
        call.enqueue(object : Callback<List<RetroPhoto>>{
            override fun onResponse(call: Call<List<RetroPhoto>>, response: Response<List<RetroPhoto>>) {
//                progressDoalog.dismiss()
                generateDataList(response.body())
            }

            override fun onFailure(call: Call<List<RetroPhoto>>, t: Throwable) {
//                progressDoalog.dismiss()
                Toast.makeText(this@QuoteActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
            }
        })

        viewManager = LinearLayoutManager(this)
//        viewAdapter = CustomAdapter(this, myDataset)
    }

    private fun generateDataList(photoList: List<RetroPhoto>?) {
        recyclerView = findViewById(R.id.my_recycler_view)
        viewAdapter = CustomAdapter(this, photoList)
        val layoutManager = LinearLayoutManager(this@QuoteActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = viewAdapter
    }
}
