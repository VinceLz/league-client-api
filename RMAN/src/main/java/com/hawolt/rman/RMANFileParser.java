package com.hawolt.rman;

import com.github.luben.zstd.Zstd;
import com.hawolt.rman.body.*;
import com.hawolt.rman.header.RMANFileHeader;
import com.hawolt.rman.util.RandomAccessReader;
import com.hawolt.rman.util.VTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 01/01/2023 03:07
 * Author: Twitter @hawolt
 **/

public class RMANFileParser {

    public static RMANFile parse(byte[] b) {
        RandomAccessReader reader = new RandomAccessReader(b);
        RMANFile file = new RMANFile();
        RMANFileHeader header = header(reader);
        file.setHeader(header);
        reader.seek(header.getOffset());
        file.setCompressedBody(reader.readBytes(header.getLength()));
        if (header.getSignatureType() != 0) {
            file.setSignature(reader.readBytes(256));
        }
        file.setBody(body(file));
        file.buildChunkMap();
        file.buildBundleMap();
        return file;
    }

    private static RMANFileBody body(RMANFile file) {
        byte[] uncompressed = Zstd.decompress(file.getCompressedBody(), file.getHeader().getDecompressedLength());
        RandomAccessReader reader = new RandomAccessReader(uncompressed);
        RMANFileBody body = new RMANFileBody();
        body.setHeaderOffset(reader.readInt());
        reader.seek(body.getHeaderOffset());

        RMANFileBodyHeader header = new RMANFileBodyHeader();
        header.setTableOffset(reader.readInt());

        int offset = reader.readInt();
        reader.seek(reader.position() + offset);
        header.setBundleListOffset(reader.position() - 4);
        reader.seek(reader.position() - offset);

        offset = reader.readInt();
        reader.seek(reader.position() + offset);
        header.setLanguageListOffset(reader.position() - 4);
        reader.seek(reader.position() - offset);

        offset = reader.readInt();
        reader.seek(reader.position() + offset);
        header.setFileListOffset(reader.position() - 4);
        reader.seek(reader.position() - offset);

        offset = reader.readInt();
        reader.seek(reader.position() + offset);
        header.setFolderListOffset(reader.position() - 4);
        reader.seek(reader.position() - offset);

        offset = reader.readInt();
        reader.seek(reader.position() + offset);
        header.setKeyHeaderOffset(reader.position() - 4);
        reader.seek(reader.position() - offset);

        offset = reader.readInt();
        reader.seek(reader.position() + offset);
        header.setUnknownOffset(reader.position() - 4);
        reader.seek(reader.position() - offset);

        body.setHeader(header);
        body.setFiles(files(reader, header));
        body.setBundles(bundle(reader, header));
        body.setLanguages(languages(reader, header));
        body.setDirectories(directories(reader, header));

        return body;
    }

    private static List<RMANFileBodyFile> files(RandomAccessReader reader, RMANFileBodyHeader header) {
        List<RMANFileBodyFile> files = new ArrayList<>();
        reader.seek(header.getFileListOffset());
        int totalFiles = reader.readInt();
        for (int i = 0; i < totalFiles; i++) {
            RMANFileBodyFile file = new RMANFileBodyFile();
            file.setOffset(reader.readInt());
            int nextFileOffset = reader.position();
            reader.seek(nextFileOffset + file.getOffset() - 4);
            file.setTableOffset(reader.readInt());
            int tmpA = reader.readInt();
            int restoreOffset = 4;
            file.setCustomNameOffset(tmpA & 0xFFFFFF);
            file.setFileTypeFlag(tmpA >> 24);
            boolean isNameInline = reader.readInt() < 100;
            reader.seek(reader.position() - 4);
            if (isNameInline) {
                file.setNameOffset(file.getCustomNameOffset());
            } else {
                file.setNameOffset(reader.readInt());
                restoreOffset = 8;
            }
            reader.seek(reader.position() + file.getNameOffset() - 4);
            file.setName(reader.readString(reader.readInt()));
            reader.seek(nextFileOffset + file.getOffset() + restoreOffset);
            file.setStructSize(reader.readInt());
            file.setSymlinkOffset(reader.readInt());
            reader.seek(reader.position() + file.getSymlinkOffset() - 4);
            file.setSymLink(reader.readString(reader.readInt()));
            reader.seek(nextFileOffset + file.getOffset() + 8 + restoreOffset);
            file.setId(reader.readLong());
            if (file.getStructSize() > 28) {
                file.setDirectoryId(reader.readLong());
            }
            file.setFileSize(reader.readInt());
            file.setPermission(reader.readInt());
            if (file.getStructSize() > 36) {
                file.setLanguageId(reader.readInt());
                file.setUnknown2(reader.readInt());
            }
            file.setSingleChunk(reader.readInt());
            if (!file.isSingleChunk()) {
                int totalChunks = reader.readInt();
                for (int j = 0; j < totalChunks; j++) {
                    file.addChunkId(reader.readLong());
                }
            } else {
                file.addChunkId(reader.readLong());
                file.setUnknown3(reader.readInt());
            }
            reader.seek(nextFileOffset);
            files.add(file);
        }
        return files;
    }

    private static List<RMANFileBodyDirectory> directories(RandomAccessReader reader, RMANFileBodyHeader header) {
        List<RMANFileBodyDirectory> directories = new ArrayList<>();
        reader.seek(header.getFolderListOffset());
        int totalDirectories = reader.readInt();
        for (int i = 0; i < totalDirectories; i++) {
            RMANFileBodyDirectory directory = new RMANFileBodyDirectory();
            directory.setOffset(reader.readInt());
            int nextFileOffset = reader.position();
            reader.seek(nextFileOffset + directory.getOffset() - 4);
            directory.setTableOffset(reader.readInt());
            int resumeOffset = reader.position();
            reader.seek(reader.position() - directory.getTableOffset());
            directory.setDirectoryIdOffset(reader.readShort());
            directory.setParentIdOffset(reader.readShort());
            reader.seek(resumeOffset);
            directory.setNameOffset(reader.readInt());
            reader.seek(reader.position() + directory.getNameOffset() - 4);
            directory.setName(reader.readString(reader.readInt()));
            reader.seek(nextFileOffset + directory.getOffset() + 4);
            if (directory.getDirectoryIdOffset() > 0) directory.setId(reader.readLong());
            if (directory.getParentIdOffset() > 0) directory.setParentId(reader.readShort());
            reader.seek(nextFileOffset);
            directories.add(directory);
        }
        return directories;
    }

    private static List<RMANFileBodyLanguage> languages(RandomAccessReader reader, RMANFileBodyHeader header) {
        List<RMANFileBodyLanguage> languages = new ArrayList<>();
        reader.seek(header.getLanguageListOffset());
        int totalLanguages = reader.readInt();
        for (int i = 0; i < totalLanguages; i++) {
            RMANFileBodyLanguage language = new RMANFileBodyLanguage();
            language.setOffset(reader.readInt());
            int nextLanguageOffset = reader.position();
            reader.seek(nextLanguageOffset + language.getOffset() - 4);
            language.setTableOffset(reader.readInt());
            language.setId(reader.readInt());
            language.setNameOffset(reader.readInt());
            reader.seek(reader.position() + language.getNameOffset() - 4);
            language.setName(reader.readString(reader.readInt()));
            reader.seek(nextLanguageOffset);
            languages.add(language);
        }
        return languages;
    }

    private static List<RMANFileBodyBundle> bundle(RandomAccessReader reader, RMANFileBodyHeader header) {
        List<RMANFileBodyBundle> bundles = new ArrayList<>();
        reader.seek(header.getBundleListOffset());
        int totalBundles = reader.readInt();
        for (int i = 0; i < totalBundles; i++) {
            RMANFileBodyBundle bundle = new RMANFileBodyBundle();
            bundle.setOffset(reader.readInt());
            int nextBundleOffset = reader.position();
            reader.seek(nextBundleOffset + bundle.getOffset() - 4);
            bundle.setTableOffset(reader.readInt());
            bundle.setHeaderSize(reader.readInt());
            if (bundle.getHeaderSize() > 12) {
                bundle.setSkipped(reader.readBytes(bundle.getHeaderSize() - 12));
            }
            reader.seek(nextBundleOffset + (bundle.getOffset() - bundle.getTableOffset()) - 4);
            bundle.setVTable(VTable.read(reader));

            reader.seek(nextBundleOffset + bundle.getOffset() - 4 + bundle.getVTable().getOffsets()[0]);
            bundle.setId(reader.readLong());

            reader.seek(nextBundleOffset + bundle.getOffset() - 4 + bundle.getVTable().getOffsets()[1]);
            int chunkOffsetOffset = reader.readInt();
            reader.seek(nextBundleOffset + bundle.getOffset() - 4 + bundle.getVTable().getOffsets()[1] + chunkOffsetOffset);
            int chunkOffsetLength = reader.readInt();

            List<RMANFileBodyBundleChunk> chunks = new ArrayList<>();
            for (int j = 0; j < chunkOffsetLength; j++) {
                int chunkOffset = reader.readInt();
                int nextChunkOffset = reader.position();
                reader.seek(chunkOffset + nextChunkOffset - 4);
                RMANFileBodyBundleChunk chunk = new RMANFileBodyBundleChunk();
                chunk.setTableOffset(reader.readInt());
                chunk.setCompressedSize(reader.readInt());
                chunk.setUncompressedSize(reader.readInt());
                chunk.setId(reader.readLong());
                chunks.add(chunk);
                reader.seek(nextChunkOffset);
            }
            bundle.setChunks(chunks);
            reader.seek(nextBundleOffset);
            bundles.add(bundle);
        }
        return bundles;
    }

    private static RMANFileHeader header(RandomAccessReader reader) {
        RMANFileHeader header = new RMANFileHeader();
        header.setMagic(reader.readString(4));
        header.setMajor(reader.readByte());
        header.setMinor(reader.readByte());
        header.setUnknown(reader.readByte());
        header.setSignatureType(reader.readByte());
        header.setOffset(reader.readInt());
        header.setLength(reader.readInt());
        header.setManifestId(reader.readLong());
        header.setDecompressedLength(reader.readInt());
        return header;
    }
}
;