package com.hawolt.version.local;

import com.hawolt.generic.data.Platform;
import com.hawolt.generic.runnable.IExceptionCallback;
import com.hawolt.generic.value.impl.PairedValueSupplier;
import com.hawolt.logger.Logger;
import com.hawolt.manifest.KeystoneFoundation;
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

import java.util.List;

/**
 * Created: 13/01/2023 00:26
 * Author: Twitter @hawolt
 **/

public class LocalRiotFileVersion extends PairedValueSupplier<String, String> implements IRMANResource, IVersionSupplier {

    private final List<String> files;

    public LocalRiotFileVersion(List<String> files) {
        this(null, files);
    }

    public LocalRiotFileVersion(IExceptionCallback callback, List<String> files) {
        super(callback);
        this.files = files;
        this.run();
    }

    @Override
    protected void execute() throws Exception {
        KeystoneFoundation keystoneFoundation = new KeystoneFoundation(getLocation());
        ManifestLoader manifestLoader = new ManifestLoader(keystoneFoundation);
        RMANFile rmanFile = RMANFileParser.parse(manifestLoader.getManifest());
        for (RMANFileBodyFile rmanFileBodyFile : rmanFile.getBody().getFiles()) {
            if (getTargetFiles().contains(rmanFileBodyFile.getName())) {
                byte[] extracted = LocalSupplierUtility.downloadBundle(manifestLoader, rmanFile, rmanFileBodyFile);
                ResourceEntry[] entries = LocalSupplierUtility.getResourceEntries(extracted);
                for (ResourceEntry entry : entries) {
                    byte[] data = entry.getData();
                    VersionInfo version = ResourceParser.readVersionInfo(data);
                    StringFileInfo strings = version.getStringFileInfo();
                    StringTable table = strings.getTable(0);
                    for (int i = 0; i < table.getCount(); i++) {
                        String key = table.getString(i).getKey();
                        if (!"ProductVersion".contains(key)) continue;
                        if (PairedValueSupplier.debug) {
                            Logger.debug("[cache] store: (k:{}, v:{})", rmanFileBodyFile.getName(), table.getString(i).getValue());
                        }
                        put(rmanFileBodyFile.getName(), table.getString(i).getValue());
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTargetFiles() {
        return files;
    }

    @Override
    public String getLocation() {
        return "https://clientconfig.rpg.riotgames.com/api/v1/config/public?version=99.0.0.9999999&patchline=KeystoneFoundationLiveWin&app=Riot%20Client&namespace=keystone.self_update";
    }

    @Override
    public String getVersionValue(String file) {
        return getVersionValue(null, file);
    }

    @Override
    public String getVersionValue(Platform platform, String file) {
        return getValue(file);
    }
}
