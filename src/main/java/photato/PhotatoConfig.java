package photato;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix="photato")
@Data
public class PhotatoConfig {

    private int serverPort;
    private boolean indexPrefixModeOnly;
    private boolean indexFolderName;
    private boolean thumbnailUseParallelPicturesGeneration;
    private boolean forceFfmpegToolsDownload;
    private boolean forceExifToolsDownload;
    private int thumbnailHeight;
    private int thumbnailQuality;
    private int addressElementsCount;
	private int fullScreenPictureQuality;
	private int fullScreenMaxPictureWidth;
	private int fullScreenMaxPictureHeight;
	private String cacheDir;
	
	public String getThumbnailCacheDir() {
		return cacheDir + "/thumbnails";
	}
	
	public String getFullscreenCacheDir() {
		return cacheDir + "/fullscreen";
	}
	
	public String getExtractedCacheDir() {
		return cacheDir + "/extracted";
	}
	
}
