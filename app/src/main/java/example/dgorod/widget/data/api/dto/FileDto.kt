package example.dgorod.widget.data.api.dto

/**
 *
 * @author Dmytro Gorodnytskyi
 * on 23-May-17.
 */
data class FileDto(
        val filename: String?,
        val type: String?,
        val language: String?,
        val raw_url: String?,
        val size: Int)