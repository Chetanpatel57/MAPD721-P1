package com.happyplaces.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.happyplaces.R
import com.happyplaces.models.PlaceMarkModel
import kotlinx.android.synthetic.main.activity_place_mark_detail.*

class PlaceMarkDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_mark_detail)
        var happyPlaceDetailModel: PlaceMarkModel? = null
        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            // get the Serializable data model class with the details in it
            happyPlaceDetailModel =
                intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as PlaceMarkModel
        }
        if (happyPlaceDetailModel != null) {

            setSupportActionBar(toolbar_happy_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title

            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            tv_description.text = happyPlaceDetailModel.description
            tv_location.text = happyPlaceDetailModel.location
        }
        btn_view_on_map.setOnClickListener {
            val intent = Intent(this@PlaceMarkDetailActivity, MapActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyPlaceDetailModel)
            startActivity(intent)
        }
    }
}