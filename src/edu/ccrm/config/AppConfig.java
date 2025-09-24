package edu.ccrm.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();
    private final Path dataDir;

    private AppConfig() {
        this.dataDir = Paths.get("data").toAbsolutePath();
        try {
            if (!Files.exists(dataDir)) Files.createDirectories(dataDir);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create data dir: " + dataDir, e);
        }
    }

    public static AppConfig getInstance() { return INSTANCE; }

    public Path getDataDir() { return dataDir; }
}
