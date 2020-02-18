package com.globant.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.globant.adapter.CharacterAdapter
import com.globant.domain.entities.MarvelCharacter
import com.globant.fragments.CharacterFragmentDialog
import com.globant.myapplication.R
import com.globant.utils.Data
import com.globant.utils.Event
import com.globant.utils.SPAN_COUNT
import com.globant.utils.Status
import com.globant.utils.TAG
import com.globant.viewmodels.RecyclerCharactersViewModel
import com.globant.viewmodels.RecyclerData
import com.globant.viewmodels.RecyclerStatus
import kotlinx.android.synthetic.main.activity_main_recyclerview.fab_delete
import kotlinx.android.synthetic.main.activity_main_recyclerview.fab_refresh
import kotlinx.android.synthetic.main.activity_main_recyclerview.fab_storage
import kotlinx.android.synthetic.main.activity_main_recyclerview.progress_bar_main_activity
import kotlinx.android.synthetic.main.activity_main_recyclerview.recycler_view_characters
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersActivity : AppCompatActivity() {

    private val viewModel by viewModel<RecyclerCharactersViewModel>()
    private var adapter = CharacterAdapter { character -> showDialogFragmentCharacter(character.id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recyclerview)
        viewModel.mainState.observe(::getLifecycle, ::updateUI)
        recycler_view_characters.adapter = adapter
        recycler_view_characters.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        viewModel.requestAllCharacters()
        setListeners()
    }

    private fun updateUI(characterData: Event<RecyclerData<List<MarvelCharacter>>>) {
        when (characterData.peekContent().responseType) {
            RecyclerStatus.ERROR -> {
                hideLoading()
                showToastError()
            }
            RecyclerStatus.LOADING -> {
                hideFAB()
                showLoading()
            }
            RecyclerStatus.SUCCESSFUL -> {
                hideLoading()
                showFAB()
                characterData.peekContent().data?.let {
                    showCharacters(it)
                }
            }
            RecyclerStatus.CLEAR -> {
                showCharacters(emptyList())
            }
        }
    }

    private fun showToastName(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
    }

    private fun showToastError() {
        Toast.makeText(this, R.string.get_characters_error, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        progress_bar_main_activity.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar_main_activity.visibility = View.GONE
    }

    private fun showDialogFragmentCharacter(id: Int) {
        showLoading()
        val fragmentManager = this.supportFragmentManager
        fragmentManager.let {
            CharacterFragmentDialog.newInstance(id, this).show(it, TAG)
            hideLoading()
        }
    }

    private fun setListeners() {
        fab_refresh.setOnClickListener { viewModel.onRefreshFABClicked() }
        fab_delete.setOnClickListener { viewModel.onDeleteFABClicked() }
        fab_storage.setOnClickListener { viewModel.onStorageFABClicked() }
    }

    fun showCharacters(characters: List<MarvelCharacter>) {
        adapter.data = characters
    }

    private fun hideFAB() {
        fab_storage.visibility = View.GONE
        fab_refresh.visibility = View.GONE
        fab_delete.visibility = View.GONE
    }
    private fun showFAB() {
        fab_storage.visibility = View.VISIBLE
        fab_refresh.visibility = View.VISIBLE
        fab_delete.visibility = View.VISIBLE
    }
}