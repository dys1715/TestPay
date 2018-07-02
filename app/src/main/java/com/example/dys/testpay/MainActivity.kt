package com.example.dys.testpay

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "H5 pay demo", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        ali_pay.setOnClickListener {
            val aliUrl = "https://secure.tlbl.winsion.net/pay/pay/prepayAlipay"
            startActivity(Intent(this, TestPayActivity::class.java)
                    .putExtra("url", aliUrl))
        }

        wx_pay.setOnClickListener {
            val wxUrl = "https://secure.tlbl.winsion.net/pay/pay/prepayWx"
            startActivity(Intent(this, TestPayActivity::class.java)
                    .putExtra("url", wxUrl))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
