package com.insubria.cookingapp.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.insubria.cookingapp.R
import com.insubria.cookingapp.auth.FirebaseAuthentication
import com.insubria.cookingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var logoutButton: Button
    private lateinit var navHeaderTitle: TextView

    private val auth: FirebaseAuthentication = FirebaseAuthentication()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddRecipeActivity::class.java)
            startActivity(intent)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Logout button logic
        logoutButton = navView.findViewById(R.id.log_out_button)
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Set the user's email in the navigation header if authenticated
        val currentUser = auth.getCurrentUser()
        if (currentUser != null) {
            // Get the header view of the NavigationView and set the email
            val headerView = navView.getHeaderView(0)
            navHeaderTitle = headerView.findViewById(R.id.nav_header_title)
            navHeaderTitle.text = currentUser.email ?: "Email non disponibile"
        }

        // Navigation configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_conversion
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
