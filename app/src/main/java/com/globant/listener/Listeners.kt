package com.globant.listener

import com.globant.domain.entities.Comic
import com.globant.domain.entities.MarvelCharacter

typealias CharacterListener = (MarvelCharacter) -> Unit
typealias ComicListener = (Comic) -> Unit
