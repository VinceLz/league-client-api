package com.hawolt.version.local;

import com.hawolt.generic.data.Platform;
import com.hawolt.generic.runnable.IExceptionCallback;
import com.hawolt.generic.value.impl.PairedValueSupplier;
import com.hawolt.logger.Logger;
import com.hawolt.manifest.LeaguePatchline;
import com.hawolt.manifest.LeagueRegionalPatchline;
import com.hawolt.manifest.ManifestLoader;
import com.hawolt.rman.RMANFile;
import com.hawolt.rman.RMANFileParser;
import com.hawolt.rman.body.RMANFileBodyFile;
import com.hawolt.rman.util.IRMANResource;
import com.hawolt.version.IVersionSupplier;
import org.boris.pecoff4j.ResourceEntry;
import org.boris.pecoff4j.io.ResourceParser;
import org.boris.pecoff4j.resources.StringFileInfo;
import org.boris.pecoff4j.resources.StringTable;
import org.boris.pecoff4j.resources.VersionInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: 13/01/2023 10:31
 * Author: Twitter @hawolt
 **/

public class LocalLeagueFileVersion extends PairedValueSupplier<Platform, Map<String, String>> implements IRMANResource, IVersionSupplier {

    private final List<Platform> platforms;
    private final List<String> files;

    public LocalLeagueFileVersion(List<String> files, Platform... platforms) {
        this(null, files, platforms);
    }

    public LocalLeagueFileVersion(IExceptionCallback callback, List<String> files, Platform... platforms) {
        super(callback);
        this.platforms = Arrays.asList(platforms);
        this.files = files;
        this.run();
    }

    @Override
    protected void execute() throws Exception {
        LeaguePatchline patchline = new LeaguePatchline(getLocation());
        List<LeagueRegionalPatchline> regionalPatchlines = patchline.load();
        for (LeagueRegionalPatchline regionalPatchline : regionalPatchlines) {
            Platform platform = Platform.findByFriendlyName(regionalPatchline.getId());
            if (platform == null || !platforms.contains(platform)) continue;
            ManifestLoader manifestLoader = new ManifestLoader(regionalPatchline.getUrl());
            RMANFile rmanFile = RMANFileParser.parse(manifestLoader.getManifest());
            for (RMANFileBodyFile rmanFileBodyFile : rmanFile.getBody().getFiles()) {
                if (getTargetFiles().contains(rmanFileBodyFile.getName())) {
                    byte[] extracted = LocalSupplierUtility.downloadBundle(manifestLoader, rmanFile, rmanFileBodyFile);
                    ResourceEntry[] entries = LocalSupplierUtility.getResourceEntries(extracted);
                    handle(platform, rmanFileBodyFile.getName(), entries);
                }
            }
        }
    }

    private void handle(Platform platform, String name, ResourceEntry[] entries) throws IOException {
        for (ResourceEntry entry : entries) {
            byte[] data = entry.getData();
            VersionInfo version = ResourceParser.readVersionInfo(data);
            StringFileInfo strings = version.getStringFileInfo();
            StringTable table = strings.getTable(0);
            for (int i = 0; i < table.getCount(); i++) {
                String key = table.getString(i).getKey();
                if (!"ProductVersion".contains(key)) continue;
                if (!containsKey(platform)) put(platform, new HashMap<>());
                if (PairedValueSupplier.debug) {
                    Logger.debug("[cache] store: (o:{}, k:{}, v:{})", platform, name, table.getString(i).getValue());
                }
                getValue(platform).put(name, table.getString(i).getValue());
            }
        }
    }

    @Override
    public List<String> getTargetFiles() {
        return files;
    }

    @Override
    public String getLocation() {
        return "https://clientconfig.rpg.riotgames.com/api/v1/config/public?namespace=keystone.products.league_of_legends.patchlines";
    }

    @Override
    public String getVersionValue(String file) {
        throw new RuntimeException("Unable to obtain version for file " + file + " without Region");
    }

    @Override
    public String getVersionValue(Platform platform, String file) {
        return getValue(platform).get(file);
    }
}
