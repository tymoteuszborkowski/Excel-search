package pl.tymoteuszborkowski.threads;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class SearchingThread implements Runnable {

    private static final String[] EXTENSIONS = new String[]{"gz", "gt", "sibbox", "xml", "lbl"};

    private List<String> filenames;
    private File folderLocalization;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<String>> foundedAndNotFounded;


    SearchingThread(List<String> filenames, File folderLocalization, CopyOnWriteArrayList<CopyOnWriteArrayList<String>> foundedAndNotFounded) {
        this.filenames = filenames;
        this.folderLocalization = folderLocalization;
        this.foundedAndNotFounded = foundedAndNotFounded;
    }


    @Override
    public void run() {

        Collection files;

        //temporary list to store only names of founded files
        final CopyOnWriteArrayList<String> tempList = new CopyOnWriteArrayList<>();
        String[] filenamesArray = filenames.stream().toArray(String[]::new);


        // check iterated file by every of passed filenames

        try {
            files = FileUtils.listFiles(folderLocalization, null, true);

            for (Object file1 : files) {
                File file = (File) file1;

                for (String name : filenamesArray) {
                    if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(name))) {
                        if ((FilenameUtils.isExtension(file.getName(), EXTENSIONS))) {
                            foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                            tempList.add(FilenameUtils.getBaseName(file.getName()));
                        }
                    }
                }
            }


            for (String name : filenamesArray) {
                if (!tempList.contains(name))
                    foundedAndNotFounded.get(1).add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}