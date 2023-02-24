package com.hawolt.version.local;

import com.hawolt.manifest.ManifestLoader;
import com.hawolt.rman.RMANFile;
import com.hawolt.rman.body.RMANFileBodyBundle;
import com.hawolt.rman.body.RMANFileBodyFile;
import com.hawolt.rman.io.downloader.Bundle;
import com.hawolt.rman.io.downloader.BundleDownloader;
import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.ResourceDirectory;
import org.boris.pecoff4j.ResourceEntry;
import org.boris.pecoff4j.constant.ResourceType;
import org.boris.pecoff4j.io.PEParser;
import org.boris.pecoff4j.util.ResourceHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created: 13/01/2023 10:25
 * Author: Twitter @hawolt
 **/

public class LocalSupplierUtility {
    public static byte[] downloadBundle(ManifestLoader manifestLoader, RMANFile rmanFile, RMANFileBodyFile rmanFileBodyFile) throws IOException {
        Set<RMANFileBodyBundle> bundles = rmanFile.getBundlesForFile(rmanFileBodyFile);
        List<Bundle> list = BundleDownloader.download(manifestLoader.getType(), bundles);
        return rmanFile.extract(rmanFileBodyFile, list);
    }

    public static ResourceEntry[] getResourceEntries(byte[] extracted) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(extracted);
        PE pe = PEParser.parse(stream);
        ResourceDirectory rd = pe.getImageData().getResourceTable();
        return ResourceHelper.findResources(rd, ResourceType.VERSION_INFO);
    }
}
