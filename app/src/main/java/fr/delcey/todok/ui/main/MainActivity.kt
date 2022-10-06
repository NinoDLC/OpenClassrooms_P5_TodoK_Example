package fr.delcey.todok.ui.main

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import fr.delcey.todok.databinding.MainActivityBinding
import fr.delcey.todok.domain.navigate.model.DestinationEntity
import fr.delcey.todok.ui.BaseActivity
import fr.delcey.todok.ui.utils.viewBinding

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by viewBinding { MainActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            navigator.navigate(DestinationEntity.Fragment.Tasks)
        }
    }
}