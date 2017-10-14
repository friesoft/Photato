package photato.helpers;

import java.nio.file.Path;

public class MediaHelper {

	public static final String[] supportedPictureExtensions = new String[] { "jpg", "jpeg", "png", "bmp" };
	public static final String[] supportedVideoExtensions = new String[] { "mp4", "webm" };
	
    public static boolean isPictureFile(Path path) {
        String pathStr = path.toString().toLowerCase();
        String extension = FileHelper.getExtension(pathStr);
        for (String supportedExtension : supportedPictureExtensions) {
            if (supportedExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isVideoFile(Path path) {
        String pathStr = path.toString().toLowerCase();
        String extension = FileHelper.getExtension(pathStr);
        for (String supportedExtension : supportedVideoExtensions) {
            if (supportedExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
    }
}
