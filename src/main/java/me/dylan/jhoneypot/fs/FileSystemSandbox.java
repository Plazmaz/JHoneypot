package me.dylan.jhoneypot.fs;


import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class FileSystemSandbox {
    private File directory;
    private @Getter String location = "/";

    private String[] folders = new String[] {
        "bin",
        "etc",
        "etc",
        "root",
        "tmp",
        "usr",
        "var"
    };

    public FileSystemSandbox(String path) {
        directory = new File(path);
        if(!directory.exists()) {
            directory.mkdirs();
            initializeFileStructure(directory);
        }
    }

    public void initializeFileStructure(File root) {
        for(String folder : folders) {
            new File(root, folder).mkdir();
        }
    }

    public boolean cdUp() {
        if(location.contains("/")) {
            int firstIdx = location.indexOf("/");
            int lastIdx = location.lastIndexOf("/");
            if(firstIdx >= lastIdx) {
                location = "/";
            } else {
                location = "/" + location.substring(firstIdx + 1, location.lastIndexOf("/"));
            }
        }
        return cdTo(location);
    }

    public boolean cdTo(String path) {
        try {
            File toFile;
            path = path.replace("/", File.separator);
            if(path.startsWith(File.separator)) {
                toFile = getFile(directory.getAbsolutePath() + path);
            } else {
                toFile = getFile(directory.getAbsolutePath() + location  + File.separator + path);
            }
            location = toFile.isDirectory() ?  "/" + path : location;
            return toFile.isDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String ls() {
        String out = "";
        try {
            String path = directory.getPath() + location.replace("/", File.separator);
            if(getFile(path).list() != null) {
                for (String str : getFile(path).list()) {
                    out += " " + str;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public void mkDir(String directory) throws IOException {
        directory = directory.replace('\\', File.separatorChar);
        File newDir = getFile(this.directory.getPath() + location + File.separatorChar + directory);
        newDir.mkdirs();
    }

    public String cat(String path) throws Exception {
        File file = getFile(directory.getPath() + location + "/" + path);
        if(file == null || !file.exists()) {
            throw new Exception("cat: " + path + ": No such file or directory");
        } else if(!file.isFile()) {
            throw new Exception("cat: " + path + ": Is a directory");
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String contents = "";
            String line;
            while((line = reader.readLine()) != null) {
                contents += line;
            }
            return contents;
        }
    }

    public File getFile(String path) throws IOException {
        path = path.replace("/", File.separator);
        File newFile = new File(path);
        String sanitizedPath = newFile.getCanonicalPath();
        if(sanitizedPath.startsWith(directory.getCanonicalPath())) {
            return newFile;
        } else {
            return directory;
        }
    }

    public File getRoot() {
        return directory;
    }
}
