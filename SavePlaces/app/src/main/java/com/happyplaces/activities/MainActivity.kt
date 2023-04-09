package com.happyplaces.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happyplaces.R
import com.happyplaces.adapters.PlacesMarkAdapter
import com.happyplaces.database.DatabaseHandler
import com.happyplaces.models.PlaceMarkModel
import com.happyplaces.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddPlaceMarkActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlacesListFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyPlacesListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }
    private fun getHappyPlacesListFromLocalDB() {

        val dbHandler = DatabaseHandler(this)

        val getHappyPlacesList = dbHandler.getHappyPlacesList()

        if (getHappyPlacesList.size > 0) {
            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlacesList)
        } else {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }
    private fun setupHappyPlacesRecyclerView(happyPlacesList: ArrayList<PlaceMarkModel>) {

        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = PlacesMarkAdapter(this, happyPlacesList)
        rv_happy_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object :
            PlacesMarkAdapter.OnClickListener {
            override fun onClick(position: Int, model: PlaceMarkModel) {
                val intent = Intent(this@MainActivity, PlaceMarkDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })
        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as PlacesMarkAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)
        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as PlacesMarkAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlacesListFromLocalDB() // Gets the latest list from the local database after item being delete from it.
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_happy_places_list)
    }

    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}