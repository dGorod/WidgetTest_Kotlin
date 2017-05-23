package example.dgorod.widget.data.api

import example.dgorod.widget.data.api.dto.GistDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 23-May-17.
 */
interface ApiInterface {

    @GET("users/{username}/gists")
    fun getGists(@Path("username") username: String): Single<List<GistDto>>
}