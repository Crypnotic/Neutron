package me.crypnotic.neutron.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import me.crypnotic.neutron.NeutronPlugin;

public class FileIO {

    public static File getOrCreate(Path path, String name) {
        File file = new File(path.toFile(), name);
        if (!file.exists()) {
            try {
                try (InputStream input = NeutronPlugin.class.getResourceAsStream("/" + name)) {
                    if (input != null) {
                        Files.copy(input, file.toPath());
                    } else {
                        file.createNewFile();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return file;
    }
}
