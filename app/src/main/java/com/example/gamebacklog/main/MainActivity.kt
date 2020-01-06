package com.example.gamebacklog.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebacklog.R
import com.example.gamebacklog.add.AddActivity
import com.example.gamebacklog.model.Game
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private val games = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(games)

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        rvGames.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        rvGames.adapter = gameAdapter

        fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        btnDelete.setOnClickListener { onDeleteGamesButtonClick() }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.game.observe(this, Observer { gameList ->
            if (gameList != null) {
                games.clear()
                games.addAll(gameList)
                gameAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun onDeleteGamesButtonClick() {
        if (games.isNotEmpty()) {
            val gameToBeDeleted = games.first()
            games.remove(games.first())
            val snackbar = Snackbar.make(
                content,
                getString(R.string.item_deleted, gameToBeDeleted.title),
                Snackbar.LENGTH_LONG
            )
            snackbar.setAction(getString(R.string.undo)) {
                onUndoClick(gameToBeDeleted)
            }
            snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event == DISMISS_EVENT_TIMEOUT) {
                        mainViewModel.deleteGame(gameToBeDeleted)
                    }
                    super.onDismissed(transientBottomBar, event)
                }
            })
            snackbar.show()
            gameAdapter.notifyDataSetChanged()
        }
    }

    private fun onUndoClick(game: Game) {
        games.add(0, game)
        gameAdapter.notifyDataSetChanged()
    }
}
