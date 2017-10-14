package photato;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhotatoApplication {

	@Autowired
	private PhotatoConfig config;

	public static void main(String[] args) {
		SpringApplication.run(PhotatoApplication.class, args);

		/*
	        .registerHandler(Routes.rawVideosRootUrl + "/*", new VideoHandler(rootFolder, Routes.rawVideosRootUrl))
	        .registerHandler(Routes.rawPicturesRootUrl + "/*", new ImageHandler(rootFolder, Routes.rawPicturesRootUrl))
	        .registerHandler(Routes.fullScreenPicturesRootUrl + "/*", new ImageHandler(fileSystem.getPath(fullscreenCacheFolder), Routes.fullScreenPicturesRootUrl))
	        .registerHandler(Routes.thumbnailRootUrl + "/*", new ImageHandler(fileSystem.getPath(thumbnailCacheFolder), Routes.thumbnailRootUrl))
	        .registerHandler(Routes.listItemsApiUrl, new FolderListHandler(Routes.listItemsApiUrl, photatoFilesManager))
		
		FileSystem fileSystem = FileSystems.getDefault();
		if (!Files.exists(fileSystem.getPath("cache"))) {
			Files.createDirectory(fileSystem.getPath("cache"));
		}
		HttpClient httpClient = HttpClientBuilder.create().build();

		ExifToolDownloader.run(httpClient, fileSystem, config.isForceExifToolsDownload());
		FfmpegDownloader.run(httpClient, fileSystem, config.isForceFfmpegToolsDownload());

		ThumbnailGenerator thumbnailGenerator = new ThumbnailGenerator(fileSystem, rootFolder, config.getThumbnailCacheDir(), config.getExtractedCacheDir(),
				config.getThumbnailHeight(), config.getThumbnailQuality());
		IGpsCoordinatesDescriptionGetter gpsCoordinatesDescriptionGetter = new OSMGpsCoordinatesDescriptionGetter(httpClient, config.getAddressElementsCount());
		MetadataAggregator metadataGetter = new MetadataAggregator(fileSystem, "cache/metadata.cache", gpsCoordinatesDescriptionGetter);
		FullScreenImageGetter fullScreenImageGetter = new FullScreenImageGetter(fileSystem, rootFolder, config.getFullscreenCacheDir(),
				config.getExtractedCacheDir(), config.getFullScreenPictureQuality(), config.getFullScreenMaxPictureWidth(),
				config.getFullScreenMaxPictureHeight());

		PhotatoFilesManager photatoFilesManager = new PhotatoFilesManager(rootFolder, fileSystem, metadataGetter, thumbnailGenerator, fullScreenImageGetter,
				config.isIndexPrefixModeOnly(), config.isIndexFolderName(), config.isThumbnailUseParallelPicturesGeneration());
	*/
	}


}