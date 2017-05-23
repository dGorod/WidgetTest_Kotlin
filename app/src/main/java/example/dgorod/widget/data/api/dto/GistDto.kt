package example.dgorod.widget.data.api.dto

import com.squareup.moshi.Json

/**
 * @author Dmytro Gorodnytskyi
 * *         on 18-Apr-17.
 */

data class GistDto(
        val id: String?,
        val description: String?,
        val created_at: String?, //"2010-04-14T02:15:15Z"
        val updated_at: String?,
        val files: Map<String, FileDto> = emptyMap(),

        @Json(name = "html_url")
        val url: String?,

        @Json(name = "public")
        val isPublic: Boolean)
