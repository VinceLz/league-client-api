package com.hawolt.manifest;

import com.hawolt.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created: 13/01/2023 16:45
 * Author: Twitter @hawolt
 **/

public class RMANCache {
    private static final Path path = Paths.get(System.getProperty("java.io.tmpdir")).resolve("client-rman-cache");
    private static final Set<String> cache = new HashSet<>();

    static {
        try {
            Files.createDirectories(path);
            File[] files = path.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    cache.add(file.getName());
                }
            }
        } catch (IOException e) {
            Logger.debug("Unable to create cache directory");
        }
    }

    public static boolean isCached(String name) {
        return cache.contains(name);
    }

    public static byte[] load(String name) throws IOException {
        return Files.readAllBytes(path.resolve(name));
    }

    public static void store(String name, Path origin) throws IOException {
        if (!path.toFile().exists()) return;
        Logger.debug("[rman-cache] storing file in cache: {}", name);
        Files.move(origin, path.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        cache.add(name);
    }

    public static void store(String name, byte[] b) throws IOException {
        if (!path.toFile().exists()) return;
        Logger.debug("[rman-cache] storing file in cache: {}", name);
        Files.write(path.resolve(name), b, StandardOpenOption.CREATE);
        cache.add(name);
    }
}
