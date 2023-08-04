package com.hawolt.yaml.impl;

import com.hawolt.generic.data.Platform;
import com.hawolt.generic.runnable.IExceptionCallback;
import com.hawolt.generic.value.impl.PairedValueSupplier;
import com.hawolt.logger.Logger;
import com.hawolt.manifest.LeaguePatchline;
import com.hawolt.manifest.LeagueRegionalPatchline;
import com.hawolt.manifest.ManifestLoader;
import com.hawolt.rman.RMANFile;
import com.hawolt.rman.RMANFileParser;
import com.hawolt.rman.body.RMANFileBodyBundle;
import com.hawolt.rman.body.RMANFileBodyFile;
import com.hawolt.rman.io.downloader.Bundle;
import com.hawolt.rman.io.downloader.BundleDownloader;
import com.hawolt.rman.util.IRMANResource;
import com.hawolt.yaml.IYamlSupplier;
import com.hawolt.yaml.SystemYaml;
import com.hawolt.yaml.YamlWrapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created: 10/01/2023 18:59
 * Author: Twitter @hawolt
 **/

public class YamlSupplier extends PairedValueSupplier<Platform, YamlWrapper> implements IRMANResource, IYamlSupplier {
    private final List<Platform> list;

    public YamlSupplier(Platform... platforms) {
        this(null, platforms);
    }

    public YamlSupplier(IExceptionCallback callback, Platform... platforms) {
        super(callback);
        this.list = Arrays.asList(platforms);
        this.run();
    }

    @Override
    protected void execute() throws Exception {
        LeaguePatchline patchline = new LeaguePatchline(getLocation());
        List<LeagueRegionalPatchline> regionalPatchlines = patchline.load();
        for (LeagueRegionalPatchline regionalPatchline : regionalPatchlines) {
            try {
                Platform platform = Platform.findByFriendlyName(regionalPatchline.getId());
                if (platform == null || !list.contains(platform)) continue;
                ManifestLoader manifestLoader = new ManifestLoader(regionalPatchline.getUrl());
                RMANFile rmanFile = RMANFileParser.parse(manifestLoader.getManifest());
                for (RMANFileBodyFile rmanFileBodyFile : rmanFile.getBody().getFiles()) {
                    if (getTargetFiles().contains(rmanFileBodyFile.getName())) {
                        Set<RMANFileBodyBundle> bundles = rmanFile.getBundlesForFile(rmanFileBodyFile);
                        List<Bundle> list = BundleDownloader.download(manifestLoader.getType(), bundles);
                        byte[] extracted = rmanFile.extract(rmanFileBodyFile, list);
                        JSONObject object = SystemYaml.generate(new String(extracted)).getJSONObject(platform.name());
                        YamlWrapper wrapper = new YamlWrapper(object);
                        Logger.debug("[cache] store: (k:{}, v:{})", rmanFileBodyFile.getName(), wrapper);
                        put(platform, wrapper);
                    }
                }
            } catch (Exception e) {
                Logger.debug("Failed to parse RMAN for {}", Platform.findByFriendlyName(regionalPatchline.getId()));
            }
        }
    }

    @Override
    public YamlWrapper getYamlResources(Platform platform) throws IOException {
        return getValue(platform);
    }

    @Override
    public List<String> getTargetFiles() {
        return Collections.singletonList("system.yaml");
    }

    @Override
    public String getLocation() {
        return "https://clientconfig.rpg.riotgames.com/api/v1/config/public?namespace=keystone.products.league_of_legends.patchlines";
    }
}
