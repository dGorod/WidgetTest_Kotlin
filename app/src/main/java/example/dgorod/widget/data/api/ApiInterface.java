package example.dgorod.widget.data.api;

import java.util.List;

import example.dgorod.widget.data.api.dto.GistDto;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Dmytro Gorodnytskyi
 *         on 18-Apr-17.
 */

public interface ApiInterface {

    @GET("users/{username}/gists")
    Single<List<GistDto>> getGists(@Path("username") String username);
}
