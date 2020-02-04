package com.globant.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.globant.adapter.CharacterAdapter
import com.globant.domain.entities.MarvelCharacter
import com.globant.myapplication.R
import com.globant.utils.Data
import com.globant.utils.Event
import com.globant.utils.Status
import com.globant.viewmodels.RecyclerCharactersViewModel
import kotlinx.android.synthetic.main.activity_main_recyclerview.recycler_view_characters
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersActivity : AppCompatActivity() {

    private val viewModel by viewModel<RecyclerCharactersViewModel>()
    private var adapter = CharacterAdapter { character -> showToastName(character.name) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recyclerview)
        viewModel.mainState.observe(::getLifecycle, ::updateUI)
        recycler_view_characters.adapter = adapter
        recycler_view_characters.layoutManager = GridLayoutManager(this, 1)
        viewModel.requestAllCharacters()
    }

    private fun updateUI(characterData: Event<Data<List<MarvelCharacter>>>) {
        when (characterData.peekContent().responseType) {
            Status.ERROR -> {
                Toast.makeText(this, R.string.get_characters_error,Toast.LENGTH_SHORT).show()
            }
            Status.LOADING -> {

            }
            Status.SUCCESSFUL -> {
                characterData.peekContent().data?.let {
                    adapter.data = it
                }
            }
        }
    }

    private fun showToastName(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }
}