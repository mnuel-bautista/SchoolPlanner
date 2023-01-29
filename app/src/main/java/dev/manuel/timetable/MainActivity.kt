package dev.manuel.timetable

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
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

    lateinit var drawerLayout: DrawerLayout

    private val viewModel: MainActivityVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ContentMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        navController = findNavController(R.id.nav_host_fragment)

        addClassesToDrawer()
        drawerLayout = binding.drawerLayout
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


}
