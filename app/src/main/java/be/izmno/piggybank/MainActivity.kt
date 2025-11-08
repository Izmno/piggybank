package be.izmno.piggybank

import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create root ConstraintLayout
        val rootLayout = ConstraintLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        
        // Create FragmentContainerView
        val fragmentContainerView = FragmentContainerView(this).apply {
            id = R.id.nav_host_fragment
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            ).apply {
                bottomToTop = R.id.nav_view
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            }
        }
        
        // Create BottomNavigationView with programmatic menu
        val navView = BottomNavigationView(this).apply {
            id = R.id.nav_view
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            }
            // Create menu programmatically
            menu.add(Menu.NONE, R.id.navigation_home, Menu.NONE, getString(R.string.title_home))
                .setIcon(R.drawable.piggy_bank_icon)
            menu.add(Menu.NONE, R.id.navigation_log_entries, Menu.NONE, getString(R.string.title_log_entries))
                .setIcon(android.R.drawable.ic_menu_recent_history)
        }
        
        // Add views to root layout
        rootLayout.addView(fragmentContainerView)
        rootLayout.addView(navView)
        
        setContentView(rootLayout)

        // Create and add NavHostFragment with programmatic navigation graph
        val navHostFragment = NavHostFragment()
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()

        // Create navigation graph programmatically
        val navController = navHostFragment.navController
        
        // Create navigation graph using createGraph extension function
        val navGraph = navController.createGraph(
            id = R.id.mobile_navigation,
            startDestination = R.id.navigation_home
        ) {
            fragment<HomeFragment>(R.id.navigation_home) {
                label = getString(R.string.title_home)
            }
            fragment<LogEntriesFragment>(R.id.navigation_log_entries) {
                label = getString(R.string.title_log_entries)
            }
        }
        
        // Set the graph on the nav controller
        navController.graph = navGraph

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_log_entries
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
