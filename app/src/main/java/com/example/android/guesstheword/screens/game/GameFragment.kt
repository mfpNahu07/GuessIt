/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        Log.i("GameFragment", "Called ViewModelProvider!!!")
        //Reference to get viewModel, this is what reestablishes the connection to the same ViewModel
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)



        binding.correctButton.setOnClickListener {

            //Process the data in the viewModel
            viewModel.onCorrect()

        }

        binding.skipButton.setOnClickListener {
            viewModel.onSkip()

        }

        //Set up the observation relationship for the score LiveDatas:
        //viewModel.score.observe(this, Observer { newScore -> //Get the LiveData from your view model and call the observe method.
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, Observer { nextWord ->
            binding.wordText.text = nextWord.toString()
        })



        return binding.root

    }


    /**
     * Called when the game is finished
     * To some extent, it's deciding what happens when the game is finished. But what it does is,
     * it causes a navigation, and navigation needs access to a nav controller. Nav controller is
     * found by passing in a viewer fragment, which are things that we do not want in the viewModel.
     * Any navigation is going to need to be done in the fragment.
     */

    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
        findNavController(this).navigate(action)
    }


}
