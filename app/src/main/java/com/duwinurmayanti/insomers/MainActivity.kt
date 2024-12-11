package com.duwinurmayanti.insomers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.duwinurmayanti.insomers.activity.HistoryActivity
import com.duwinurmayanti.insomers.databinding.ActivityMainBinding
import com.duwinurmayanti.insomers.fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                    binding.bottomNavigationView.selectedItemId = R.id.nav_home
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment?.javaClass == fragment.javaClass) return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateNavigationState() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        binding.bottomNavigationView.selectedItemId = when (currentFragment) {
            is HomeFragment -> R.id.nav_home
            else -> R.id.nav_home
        }
    }

    override fun onResume() {
        super.onResume()
        updateNavigationState()
    }
}