package photato.core.resize.thumbnails;

import java.io.IOException;
import java.nio.file.Path;
import photato.core.entities.PhotatoMedia;

public interface IThumbnailGenerator {

    void generateThumbnail(PhotatoMedia media) throws IOException;

    void deleteThumbnail(Path originalFilename, long lastModifiedTimestamp) throws IOException;

    String getThumbnailUrl(Path originalFilename, long lastModifiedTimestamp);

    int getThumbnailWidth(int originalWidth, int originalHeight);

    int getThumbnailHeight(int originalWidth, int originalHeight);
}
