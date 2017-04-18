package example.dgorod.widget.data.api.dto;

/**
 * @author Dmytro Gorodnytskyi
 *         on 18-Apr-17.
 */

public class FileDto {
    private String filename;
    private String type;
    private String language;
    private String raw_url;
    private int size;

    public String getFilename() {
        return filename;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String getRaw_url() {
        return raw_url;
    }

    public int getSize() {
        return size;
    }
}
