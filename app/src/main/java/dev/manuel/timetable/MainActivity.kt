package dev.manuel.timetable

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.manuel.timetable.databinding.ContentMainBinding
import kotlinx.coroutines.launch

const val SHARED_PREFERENCES_TAG = "com.dev.mnuel.timetable.preferences"
const val SELECTED_TIMETABLE_ID = "com.dev.mnuel.timetable.preferences"
const val DEFAULT_TIMETABLE_ID = 0L

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContentMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navController: NavController

    private val viewModel: MainActivityVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ContentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        val drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.classesFragment,
                R.id.tasksFragment, R.id.notesFragment,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)


        setupFab()

        addClassesToDrawer()
        addDestinationListener()
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.action_homeFragment_to_newClassFragment)
                }
                R.id.notesFragment -> {
                    navController.navigate(R.id.action_notesFragment_to_editNoteFragment)
                }
            }
        }
    }


    private fun addDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.notesFragment -> {
                    binding.fab.show()
                }
                else -> binding.fab.hide()
            }
        }
    }

    private fun addClassesToDrawer() {
        lifecycleScope.launch {
            viewModel.classes.collect { classes ->
                classes.forEachIndexed { index, mClass ->
                    binding.navigationView.menu.add(
                        R.id.classes_menu_group,
                        mClass.id.toInt(),
                        index + 1,
                        mClass.subject
                    )
                    //The first items in the menu are Home, Tasks, Notes, and Classes. That's
                    //why I'm adding 4
                    binding.navigationView.menu[index + 4].icon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_outline_book_24, theme)
                }
            }
        }

        binding.navigationView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener when (it.itemId) {
                R.id.homeFragment, R.id.tasksFragment, R.id.classesFragment, R.id.notesFragment -> {
                    navController.navigate(
                        it.itemId,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.homeFragment, false)
                            .build()
                    )
                    true
                }
                R.id.create_class_item -> {
                    navController.navigate(R.id.action_global_newClassFragment)
                    true
                }
                else -> {
                    navController.navigate(
                        R.id.action_global_classDetailsFragment,
                        bundleOf("classId" to it.itemId.toLong())
                    )
                    true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
