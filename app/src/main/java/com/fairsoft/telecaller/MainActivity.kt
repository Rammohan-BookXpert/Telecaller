package com.fairsoft.telecaller

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fairsoft.telecaller.databinding.ActivityMainBinding
import com.fairsoft.telecaller.utils.NetworkConnectivity
import com.fairsoft.telecaller.utils.isOnline

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Getting NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        // Initializing the network dialog
        val networkDialog = Dialog(this)
        networkDialog.apply {
            setCancelable(false)
            setContentView(R.layout.custom_network_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            findViewById<TextView>(R.id.go_settings_tv).setOnClickListener {
                val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
                startActivity(intent)
            }
        }

        if (!isOnline(this)) {
            networkDialog.show()
        } else networkDialog.dismiss()

        val networkConnectivity = NetworkConnectivity(applicationContext)
        networkConnectivity.observe(this) { status ->
            if (!status) {
                networkDialog.show()
            } else networkDialog.dismiss()
        }
    }

    // This will take care of Back navigation actions
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}