package me.crypnotic.neutron.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import me.crypnotic.neutron.NeutronPlugin;

public class FileIO {

    public static File getOrCreate(Path folder, String name) {
        File file = new File(folder.toFile(), name);
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

    public static File getOrCreateDirectory(Path folder, String name) {
        File file = new File(folder.toFile(), name);
        if (file.exists()) {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdirs();
            }
        } else {
            file.mkdirs();
        }
        return file;
    }

    public static File getOrCreateLocale(Path folder, String localName) {
        File file = new File(folder.toFile(), localName);
        if (!file.exists()) {
            try {
                try (InputStream input = NeutronPlugin.class.getResourceAsStream("/locales/" + localName)) {
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
