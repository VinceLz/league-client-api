package com.hawolt.rman;

import com.github.luben.zstd.ZstdInputStream;
import com.hawolt.rman.body.*;
import com.hawolt.rman.header.RMANFileHeader;
import com.hawolt.rman.io.StreamReader;
import com.hawolt.rman.io.downloader.BadBundleException;
import com.hawolt.rman.io.downloader.Bundle;
import com.hawolt.rman.util.Hex;
import com.hawolt.rman.util.RandomAccessReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created: 01/01/2023 03:09
 * Author: Twitter @hawolt
 **/

public class RMANFile {
    private Map<Long, RMANFileBodyBundleChunkInfo> chunksById = new HashMap<>();
    private Map<Long, RMANFileBodyBundle> bundlesById = new HashMap<>();
    private RMANFileHeader header;
    private RMANFileBody body;
    private byte[] compressedBody, signature;

    public void buildChunkMap() {
        for (RMANFileBodyBundle bundle : getBody().getBundles()) {
            int currentIndex = 0;
            for (RMANFileBodyBundleChunk chunk : bundle.getChunks()) {
                RMANFileBodyBundleChunkInfo chunkInfo = new RMANFileBodyBundleChunkInfo(bundle.getId(), chunk.getId(), currentIndex, chunk.getCompressedSize());
                chunksById.put(chunk.getId(), chunkInfo);
                currentIndex += chunk.getCompressedSize();
            }
        }
    }

    public void buildBundleMap() {
        for (RMANFileBodyBundle bundle : getBody().getBundles()) {
            bundlesById.put(bundle.getId(), bundle);
        }
    }

    public Set<RMANFileBodyBundle> getBundlesForFile(RMANFileBodyFile file) {
        return file.getChunkIds()
                .stream()
                .map(getChunkMap()::get)
                .collect(Collectors.toList())
                .stream()
                .map(RMANFileBodyBundleChunkInfo::getBundleId)
                .map(getBundleMap()::get)
                .collect(Collectors.toSet());
    }

    public byte[] extract(RMANFileBodyFile file, List<Bundle> list) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            List<Long> chunkIds = file.getChunkIds();
            for (Long chunkId : chunkIds) {
                RMANFileBodyBundleChunkInfo current = chunksById.get(chunkId);
                String name = String.join(".", Hex.from(current.getBundleId()), "bundle");
                List<Bundle> bundles = list.stream().filter(bundle -> bundle.getName().equals(name)).collect(Collectors.toList());
                if (bundles.size() != 1) throw new BadBundleException("Bad Bundle: " + name);
                Bundle bundle = bundles.remove(0);
                RandomAccessReader reader = new RandomAccessReader(bundle.getBytes());
                reader.seek(current.getOffsetToChunk());
                byte[] compressedChunkData = reader.readBytes(current.getCompressedSize());
                ZstdInputStream stream = new ZstdInputStream(new ByteArrayInputStream(compressedChunkData));
                baos.write(StreamReader.from(stream));
            }
            return baos.toByteArray();
        }
    }

    public Map<Long, RMANFileBodyBundleChunkInfo> getChunkMap() {
        return chunksById;
    }

    public Map<Long, RMANFileBodyBundle> getBundleMap() {
        return bundlesById;
    }

    public RMANFileHeader getHeader() {
        return header;
    }

    public void setHeader(RMANFileHeader header) {
        this.header = header;
    }

    public RMANFileBody getBody() {
        return body;
    }

    public void setBody(RMANFileBody body) {
        this.body = body;
    }

    public byte[] getCompressedBody() {
        return compressedBody;
    }

    public void setCompressedBody(byte[] compressedBody) {
        this.compressedBody = compressedBody;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
