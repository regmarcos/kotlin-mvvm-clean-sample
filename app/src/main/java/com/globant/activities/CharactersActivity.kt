package com.globant.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.globant.domain.entities.MarvelCharacter
import com.globant.myapplication.R
import com.globant.utils.Data
import com.globant.utils.Event
import com.globant.utils.Status
import com.globant.viewmodels.RecyclerCharactersViewModel
import kotlinx.android.synthetic.main.activity_main_recyclerview.characters_button
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersActivity : AppCompatActivity() {

    private val viewModel by viewModel<RecyclerCharactersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recyclerview)

        viewModel.mainState.observe(::getLifecycle, ::updateUI)

        characters_button.setOnClickListener { viewModel.onListOfCharactersClicked()}
    }

    private fun updateUI(characterData: Event<Data<List<MarvelCharacter>>>) {
        when (characterData.peekContent().responseType) {
            Status.ERROR -> {
                Toast.makeText(this, R.string.get_characters_error,Toast.LENGTH_SHORT).show()
            }
            Status.LOADING -> {
                //TODO
            }
            Status.SUCCESSFUL -> {
                characterData.peekContent().data?.let {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("characters",it.toString())
                }
            }
        }
    }
}