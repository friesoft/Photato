package photato.core;

import photato.core.PhotatoFilesManager;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import photato.core.entities.PhotatoFolder;
import photato.core.entities.PhotatoPicture;
import photato.core.metadata.IMetadataAggregator;
import photato.core.metadata.Metadata;
import photato.helpers.Tuple;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.junit.Assert;
import org.junit.Test;
import photato.core.entities.PhotatoMedia;
import photato.core.resize.fullscreen.IFullScreenImageGetter;
import photato.core.resize.thumbnails.IThumbnailGenerator;

public class PhotatoFilesManagerTest {

    private static class ThumbnailsGeneratorMock implements IThumbnailGenerator {

        private static final Set<String> thumbnails = new HashSet<>();

        @Override
        public void generateThumbnail(PhotatoMedia media) {
            thumbnails.add(media.fsPath.toString());
        }

        @Override
        public void deleteThumbnail(Path originalFilename, long lastModifiedTimestamp) {
            thumbnails.remove(originalFilename.toString());
        }

        public boolean contains(Path originalFilename, long lastModifiedTimestamp) {
            return thumbnails.contains(originalFilename.toString());
        }

        @Override
        public String getThumbnailUrl(Path originalFilename, long lastModifiedTimestamp) {
            return "/thumbnail/" + originalFilename + "_" + lastModifiedTimestamp;
        }

        @Override
        public int getThumbnailWidth(int originalWidth, int originalHeight) {
            return 10;
        }

        @Override
        public int getThumbnailHeight(int originalWidth, int originalHeight) {
            return 20;
        }

    }

    private static class MetadataGetterMock implements IMetadataAggregator {

        private final Map<String, List<String>> map = new HashMap<>();

        public void addMetadata(Path path, String metadata) {
            List<String> l = this.map.get(path.toString());
            if (l == null) {
                l = new ArrayList<>();
                this.map.put(path.toString(), l);
            }

            l.add(metadata);
        }

        @Override
        public Metadata getMetadata(Path path, long lastModificationTimestamp) {
            Metadata metadata = new Metadata();

            List<String> tagsList = this.map.get(path.toString());
            metadata.tags = tagsList == null ? new String[0] : tagsList.toArray(new String[tagsList.size()]);
            metadata.persons = new String[0];

            return metadata;
        }

        @Override
        public Map<Path, Metadata> getMetadatas(List<Tuple<Path, Long>> paths) {
            Map<Path, Metadata> result = new HashMap<>();
            for (Tuple<Path, Long> pathTuple : paths) {
                result.put(pathTuple.o1, this.getMetadata(pathTuple.o1, pathTuple.o2));
            }

            return result;
        }

    }

    private static class FullScreenGetterMock implements IFullScreenImageGetter {

        @Override
        public void generateImage(PhotatoMedia media) throws IOException {
        }

        @Override
        public void deleteImage(PhotatoMedia media) throws IOException {
        }

        @Override
        public FileEntity getImage(PhotatoMedia media, ContentType contentType) throws IOException {
            return null;
        }

        @Override
        public String getImageUrl(Path originalFilename, long lastModifiedTimestamp) {
            return "";
        }

        @Override
        public int getImageWidth(int originalWidth, int originalHeight) {
            return 10;
        }

        @Override
        public int getImageHeight(int originalWidth, int originalHeight) {
            return 10;
        }
    }

    @Test
    public void test() throws Exception {
        
        String rootFolder = "/home/myself/images";
        int sleepDelayForWaitingWatcherThread = 5000;

        MetadataGetterMock metadataGetterMock = new MetadataGetterMock();
        ThumbnailsGeneratorMock thumbnailsGeneratorMock = new ThumbnailsGeneratorMock();
        FullScreenGetterMock fullScreenGetterMock = new FullScreenGetterMock();

        try (FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix())) {
            Files.deleteIfExists(fileSystem.getPath(rootFolder));
            Files.createDirectories(fileSystem.getPath(rootFolder));
            Files.createDirectory(fileSystem.getPath(rootFolder + "/new-york"));
            Files.createDirectories(fileSystem.getPath(rootFolder + "/cacadada/titi"));
            Files.createDirectory(fileSystem.getPath(rootFolder + "/tmptmp"));
            Files.createDirectory(fileSystem.getPath(rootFolder + "/toignore"));
            Files.createFile(fileSystem.getPath(rootFolder + "/toignore/.photatoIgnore"));
            Files.createFile(fileSystem.getPath(rootFolder + "/toignore/ghost.png"));

            Path photatoPicture1 = fileSystem.getPath("/home/myself/images/new-york/my awesome picture1.jpg");
            Path photatoPicture2 = fileSystem.getPath("/home/myself/images/cacadada/titi/cool me.png");
            Files.createFile(photatoPicture1);
            Files.createFile(photatoPicture2);

            metadataGetterMock.addMetadata(photatoPicture1, "Pierre-Arthur Edouard");
            metadataGetterMock.addMetadata(photatoPicture1, "Moi");
            metadataGetterMock.addMetadata(photatoPicture1, "Antoine Wolololo Ching");
            metadataGetterMock.addMetadata(photatoPicture1, "Central park");
            metadataGetterMock.addMetadata(photatoPicture1, "Manhattan");
            metadataGetterMock.addMetadata(photatoPicture1, "New-York");
            metadataGetterMock.addMetadata(photatoPicture1, "États-Unis d'Amérique");

            metadataGetterMock.addMetadata(photatoPicture2, "Pierre-Arthur Edouard");
            metadataGetterMock.addMetadata(photatoPicture2, "Antoine Prout");
            metadataGetterMock.addMetadata(photatoPicture2, "Mont Royal");
            metadataGetterMock.addMetadata(photatoPicture2, "Montréal");
            metadataGetterMock.addMetadata(photatoPicture2, "Québec");
            metadataGetterMock.addMetadata(photatoPicture2, "Canada");

            try (PhotatoFilesManager photatoFilesManager = new PhotatoFilesManager(fileSystem.getPath(rootFolder), fileSystem, metadataGetterMock, thumbnailsGeneratorMock, fullScreenGetterMock, true, true, false)) {
                // TEST SEARCH
                List<PhotatoMedia> res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "pierre-arthur");
                Assert.assertEquals(2, res.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images/cacadada/titi", "pierre-arthur");
                Assert.assertEquals(1, res.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images/cacadada", "pierre-arthur");
                Assert.assertEquals(1, res.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "cacadada");
                Assert.assertEquals(1, res.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "titi");
                Assert.assertEquals(1, res.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images/tmptmp", "pierre-arthur");
                Assert.assertEquals(0, res.size());

                // TEST DIRECTORY LISTING (Counts take into account ignored folders)
                List<PhotatoFolder> foldersInFolder = photatoFilesManager.getFoldersInFolder("/home/myself/images");
                Assert.assertEquals(2, foldersInFolder.size()); // Not counting empty and .photatoignoreD folders
                foldersInFolder = photatoFilesManager.getFoldersInFolder("/home/myself/cacadada/titi"); // Parent folder
                Assert.assertEquals(0, foldersInFolder.size());
                foldersInFolder = photatoFilesManager.getFoldersInFolder("/home/myself/images/cacadada");
                Assert.assertEquals(1, foldersInFolder.size());
                List<PhotatoMedia> picturesInFolder = photatoFilesManager.getMediasInFolder("/home/myself/images/cacadada/titi");
                Assert.assertEquals(1, picturesInFolder.size());

                // TEST THUMBNAIL GENERATION
                Assert.assertTrue(thumbnailsGeneratorMock.contains(photatoPicture1, 0));
                Assert.assertTrue(thumbnailsGeneratorMock.contains(photatoPicture2, 0));

                // TEST ONLINE FILE ADDITION
                Path photatoPicture3 = fileSystem.getPath("/home/myself/images/cacadada/santa.jpg"); // Picture with no metadata
                Files.createFile(photatoPicture3);
                Thread.sleep(sleepDelayForWaitingWatcherThread);
                picturesInFolder = photatoFilesManager.getMediasInFolder("/home/myself/images/cacadada");
                foldersInFolder = photatoFilesManager.getFoldersInFolder("/home/myself/images/cacadada");
                Assert.assertEquals(1, picturesInFolder.size());
                Assert.assertEquals(1, foldersInFolder.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "santa");
                Assert.assertEquals(1, res.size());
                Assert.assertTrue(thumbnailsGeneratorMock.contains(photatoPicture3, 0));

                // TEST ONLINE FILE DELETION
                Files.delete(photatoPicture3);
                Thread.sleep(sleepDelayForWaitingWatcherThread);
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "santa");
                Assert.assertEquals(0, res.size());
                Assert.assertFalse(thumbnailsGeneratorMock.contains(photatoPicture3, 0));

                // TEST ONLINE FOLDER CREATION
                Path photatoPicture4_0 = fileSystem.getPath("/home/myself/images/carry fisher");
                Path photatoPicture4_1 = fileSystem.getPath("/home/myself/images/carry fisher/vador.jpg");
                Files.createDirectory(photatoPicture4_0);
                Files.createFile(photatoPicture4_1);
                Thread.sleep(sleepDelayForWaitingWatcherThread);
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "carry fisher");
                Assert.assertEquals(1, res.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "vador");
                Assert.assertEquals(1, res.size());
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images/carry fisher", "vador");
                Assert.assertEquals(1, res.size());
                Assert.assertTrue(thumbnailsGeneratorMock.contains(photatoPicture4_1, 0));

                // TEST ONLINE MODIFICATION
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "yoda");
                Assert.assertEquals(0, res.size());
                metadataGetterMock.addMetadata(photatoPicture4_1, "yoda");
                Files.setLastModifiedTime(photatoPicture4_1, FileTime.fromMillis(System.currentTimeMillis()));
                Thread.sleep(sleepDelayForWaitingWatcherThread);
                res = photatoFilesManager.searchMediasInFolder("/home/myself/images", "yoda");
                Assert.assertEquals(1, res.size());
            }
        }
    }

}
