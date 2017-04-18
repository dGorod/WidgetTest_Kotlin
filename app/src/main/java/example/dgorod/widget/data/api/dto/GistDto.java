package example.dgorod.widget.data.api.dto;

import com.squareup.moshi.Json;

import java.util.Map;

/**
 * @author Dmytro Gorodnytskyi
 *         on 18-Apr-17.
 */

public class GistDto {
    private String id;
    private String description;
    private String created_at; //"2010-04-14T02:15:15Z"
    private String updated_at;
    private Map<String, FileDto> files;

    @Json(name = "html_url")
    private String url;

    @Json(name = "public")
    private boolean isPublic;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Map<String, FileDto> getFiles() {
        return files;
    }

    public String getUrl() {
        return url;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
