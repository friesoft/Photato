package photato.core.resize.ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import photato.helpers.CommandLineHelper;
import photato.helpers.OsHelper;
import photato.helpers.RandomManager;

public class VideoPictureExtractor {

    public static void extractPictureFromVideo(Path videoPath, Path outputPicturePath) {
        String applicationName = OsHelper.isWindows() ? "ffmpeg.exe" : "ffmpeg";
        try {
            String commandLine = applicationName + " -y -ss 0 -i \"" + videoPath + "\" -qscale:v 2 -vframes 1 \"" + outputPicturePath + "\"";

            CommandLineHelper.CommandLineResult commandLineResult = CommandLineHelper.runCommandLine(commandLine);

            if (!outputPicturePath.toFile().exists()) {
                throw new IOException("Error while executing:\n'" + commandLine + "': " + commandLineResult.errlog);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Path extractPictureFromVideoWithRandomPath(Path videoPath, Path cacheFolder) {
        if (!Files.exists(cacheFolder)) {
            try {
                Files.createDirectories(cacheFolder);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Path outputPicturePath = cacheFolder.resolve("tmp_" + RandomManager.nextInt() + ".jpg");
        extractPictureFromVideo(videoPath, outputPicturePath);
        return outputPicturePath;
    }
}
