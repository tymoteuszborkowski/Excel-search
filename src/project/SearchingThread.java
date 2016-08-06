package project;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SearchingThread implements Runnable {

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
        CopyOnWriteArrayList<String> tempList = new CopyOnWriteArrayList<>();

        String[] extensions = new String[]{"gz", "gt", "sibbox", "xml", "lbl"};


        // if's check if number of passed filenames is divided by something and if condition is true
        // loop check one file and fit as many filenames as division number is
        if ((filenames.size() % 10) == 0) {
            for (int i = 0; i < filenames.size(); i += 10) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();

                        for (int j = 0; j < 10; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }

                    }

                    for (int j = 0; j < 10; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 9) == 0) {
            for (int i = 0; i < filenames.size(); i += 9) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 9; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 9; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 8) == 0) {
            for (int i = 0; i < filenames.size(); i += 8) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 8; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 8; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 7) == 0) {
            for (int i = 0; i < filenames.size(); i += 7) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 7; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 7; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 6) == 0) {
            for (int i = 0; i < filenames.size(); i += 6) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 6; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 6; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 5) == 0) {
            for (int i = 0; i < filenames.size(); i += 5) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 5; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 5; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 4) == 0) {
            for (int i = 0; i < filenames.size(); i += 4) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 4; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 4; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 3) == 0) {
            for (int i = 0; i < filenames.size(); i += 3) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 3; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 3; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((filenames.size() % 2) == 0) {
            for (int i = 0; i < filenames.size(); i += 2) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();


                        for (int j = 0; j < 2; j++) {
                            if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i + j)))) {
                                if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                    foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                    tempList.add(FilenameUtils.getBaseName(file.getName()));
                                }
                            }

                        }
                    }

                    for (int j = 0; j < 2; j++) {
                        if (!tempList.contains(filenames.get(i + j)))
                            foundedAndNotFounded.get(1).add(filenames.get(i + j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < filenames.size(); i++) {
                try {
                    files = FileUtils.listFiles(folderLocalization, null, true);

                    for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                        File file = (File) iterator.next();

                        if (FilenameUtils.getBaseName(file.getName()).contains(FilenameUtils.getBaseName(filenames.get(i)))) {
                            if ((FilenameUtils.isExtension(file.getName(), extensions))) {
                                foundedAndNotFounded.get(0).add(file.getAbsolutePath());
                                tempList.add(FilenameUtils.getBaseName(file.getName()));
                            }
                        }
                    }

                    if (!tempList.contains(filenames.get(i)))
                        foundedAndNotFounded.get(1).add(filenames.get(i));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
