package ar.edu.utn.frba.mobile.a2019c1

class MoviesApi {
    fun getMovies(): MutableList<Movie> {
        val dataset = mutableListOf<Movie>()
        for (i in 0..30) {
            if (i % 5 == 0) {
                dataset.add(Movie("Category $i", null, true))
                continue
            }

            if (i % 2 == 0)
            //even
                dataset.add(Movie("Movie $i", R.drawable.movie_icon_1, false))
            else
            //odd
                dataset.add(Movie("Movie $i", R.drawable.movie_icon_2, false))
        }
        return dataset
    }
}