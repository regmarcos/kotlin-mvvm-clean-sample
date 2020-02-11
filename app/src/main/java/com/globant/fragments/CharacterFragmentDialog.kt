package com.globant.fragments


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.activities.CharactersActivity
import com.globant.adapter.ComicAdapter
import com.globant.domain.entities.Comic
import com.globant.domain.entities.MarvelCharacter
import com.globant.extensions.getImageByUrl
import com.globant.viewmodels.CharacterFragmentViewModel
import com.globant.myapplication.R
import com.globant.utils.DOT
import com.globant.utils.Data
import com.globant.utils.Event
import com.globant.utils.Status
import kotlinx.android.synthetic.main.character_fragment_dialog.dialog_fragment_description
import kotlinx.android.synthetic.main.character_fragment_dialog.dialog_fragment_image
import kotlinx.android.synthetic.main.character_fragment_dialog.dialog_fragment_title
import kotlinx.android.synthetic.main.character_fragment_dialog.progress_bar_fragment_dialog
import kotlinx.android.synthetic.main.character_fragment_dialog.recycler_view_comics
import kotlinx.android.synthetic.main.comic_card_view.progress_bar_card_view_comic
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterFragmentDialog : DialogFragment() {

    private val viewModel by viewModel<CharacterFragmentViewModel>()
    private var adapter = ComicAdapter { comic -> Toast.makeText(mainActivity, comic.title, Toast.LENGTH_SHORT).show() }

    companion object {
        private var characterID: Int = 0
        private lateinit var mainActivity: CharactersActivity
        fun newInstance(id: Int, activity: CharactersActivity): CharacterFragmentDialog {
            characterID = id
            mainActivity = activity
            return CharacterFragmentDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.character_fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mainState.observe(::getLifecycle, ::updateUI)
        viewModel.comicState.observe(::getLifecycle, ::updateComics)
        recycler_view_comics.adapter = adapter
        recycler_view_comics.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
        viewModel.getCharacterById(characterID)
        viewModel.getAllComics(characterID)
    }

    private fun updateUI(characterData: Event<Data<MarvelCharacter>>) {
        when (characterData.peekContent().responseType) {
            Status.ERROR -> {
                showToastError()
            }
            Status.LOADING -> {
                showLoading()
            }
            Status.SUCCESSFUL -> {
                characterData.peekContent().data?.let {
                    setCharacters(it)
                }
            }
        }
    }

    private fun updateComics(comicData: Event<Data<List<Comic>>>) {
        when (comicData.peekContent().responseType) {
            Status.ERROR -> {
                hideLoading()
                hideComicLoading()
                showToastError()
            }
            Status.LOADING -> {
                showLoading()
            }
            Status.SUCCESSFUL -> {
                comicData.peekContent().data?.let {
                    adapter.data = it
                    showComicLoading()
                }
                hideLoading()

            }
        }
    }

    private fun setCharacters(character: MarvelCharacter) {
        dialog_fragment_title.text = character.name
        dialog_fragment_description.text = character.description
        val url = "${character.thumbnail?.path}$DOT${character.thumbnail?.extension}"
        dialog_fragment_image.getImageByUrl(url)
    }

    private fun showToastError() {
        Toast.makeText(mainActivity, R.string.get_characters_error, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        progress_bar_fragment_dialog.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar_fragment_dialog.visibility = View.GONE
    }

    private fun showComicLoading() {
        progress_bar_card_view_comic?.let { it.visibility = View.VISIBLE }
    }

    private fun hideComicLoading() {
        progress_bar_card_view_comic?.let { it.visibility = View.GONE }
    }

}
