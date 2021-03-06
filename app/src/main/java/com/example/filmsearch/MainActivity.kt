package com.example.filmsearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        startActivity(intent)
    }

private lateinit var popularMovies: RecyclerView
private lateinit var popularMoviesAdapter: MoviesAdapter
private lateinit var popularMoviesLayoutMgr: LinearLayoutManager

private var popularMoviesPage = 1

    private lateinit var nowPlayingMovies: RecyclerView
    private lateinit var nowPlayingMoviesAdapter: MoviesAdapter
    private lateinit var nowPlayingMoviesLayoutMgr: LinearLayoutManager

    private var nowPlayingMoviesPage = 1

    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MoviesAdapter
    private lateinit var upcomingMoviesLayoutMgr: LinearLayoutManager

    private var upcomingMoviesPage = 1



override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    popularMovies = findViewById(R.id.popular_movies)
    popularMoviesLayoutMgr = LinearLayoutManager(
        this,
        LinearLayoutManager.HORIZONTAL,
        false
    )
    popularMovies.layoutManager = popularMoviesLayoutMgr
    popularMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
    popularMovies.adapter = popularMoviesAdapter

    nowPlayingMovies = findViewById(R.id.now_playing_movies)
    nowPlayingMoviesLayoutMgr = LinearLayoutManager(
        this,
        LinearLayoutManager.HORIZONTAL,
        false
    )
    nowPlayingMovies.layoutManager = nowPlayingMoviesLayoutMgr
    nowPlayingMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
    nowPlayingMovies.adapter = nowPlayingMoviesAdapter

    upcomingMovies = findViewById(R.id.upcoming_movies)
    upcomingMoviesLayoutMgr = LinearLayoutManager(
        this,
        LinearLayoutManager.HORIZONTAL,
        false
    )
    upcomingMovies.layoutManager = upcomingMoviesLayoutMgr
    upcomingMoviesAdapter = MoviesAdapter(mutableListOf()) { movie -> showMovieDetails(movie) }
    upcomingMovies.adapter = upcomingMoviesAdapter



    getPopularMovies()
    getUpcomingMovies()
    getNowPlayingMovies()


}

private fun getPopularMovies() {
    MoviesRepository.getPopularMovies(
        popularMoviesPage,
        ::onPopularMoviesFetched,
        ::onError
    )
}

    private fun getNowPlayingMovies() {
        MoviesRepository.getNowPlayingMovies(
            nowPlayingMoviesPage,
            ::onNowPlayingMoviesFetched,
            ::onError
        )
    }

    private fun getUpcomingMovies() {
        MoviesRepository.getUpcomingMovies(
            upcomingMoviesPage,
            ::onUpcomingMoviesFetched,
            ::onError
        )
    }

    private fun attachNowPlayingMoviesOnScrollListener() {
        nowPlayingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = nowPlayingMoviesLayoutMgr.itemCount
                val visibleItemCount = nowPlayingMoviesLayoutMgr.childCount
                val firstVisibleItem = nowPlayingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    nowPlayingMovies.removeOnScrollListener(this)
                    nowPlayingMoviesPage++
                    getNowPlayingMovies()
                }
            }
        })
    }

    private fun attachPopularMoviesOnScrollListener() {
    popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val totalItemCount = popularMoviesLayoutMgr.itemCount
            val visibleItemCount = popularMoviesLayoutMgr.childCount
            val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

            if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                popularMovies.removeOnScrollListener(this)
                popularMoviesPage++
                getPopularMovies()
            }
        }
    })
}

    private fun attachUpcomingMoviesOnScrollListener() {
        upcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = upcomingMoviesLayoutMgr.itemCount
                val visibleItemCount = upcomingMoviesLayoutMgr.childCount
                val firstVisibleItem = upcomingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    upcomingMovies.removeOnScrollListener(this)
                    upcomingMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }


    private fun onPopularMoviesFetched(movies: List<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }


    private fun onNowPlayingMoviesFetched(movies: List<Movie>) {
       nowPlayingMoviesAdapter.appendMovies(movies)
        attachNowPlayingMoviesOnScrollListener()
    }

    private fun onUpcomingMoviesFetched(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)
        attachUpcomingMoviesOnScrollListener()
    }

    private fun onError() {
    Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
}
/*
    fun goToSearchActivity(view: View?) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
    */

}
