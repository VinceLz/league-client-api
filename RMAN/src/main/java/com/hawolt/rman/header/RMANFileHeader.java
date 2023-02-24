package com.hawolt.rman.header;

/**
 * Created: 01/01/2023 03:06
 * Author: Twitter @hawolt
 **/

public class RMANFileHeader {
    private String magic;
    private byte major, minor, unknown, signatureType;
    private int offset, length, decompressedLength;
    private long manifestId;

    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public byte getMajor() {
        return major;
    }

    public void setMajor(byte major) {
        this.major = major;
    }

    public byte getMinor() {
        return minor;
    }

    public void setMinor(byte minor) {
        this.minor = minor;
    }

    public byte getUnknown() {
        return unknown;
    }

    public void setUnknown(byte unknown) {
        this.unknown = unknown;
    }

    public byte getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(byte signatureType) {
        this.signatureType = signatureType;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDecompressedLength() {
        return decompressedLength;
    }

    public void setDecompressedLength(int decompressedLength) {
        this.decompressedLength = decompressedLength;
    }

    public long getManifestId() {
        return manifestId;
    }

    public void setManifestId(long manifestId) {
        this.manifestId = manifestId;
    }

    @Override
    public String toString() {
        return "RMANFileHeader{" +
                "magic='" + magic + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                ", unknown=" + unknown +
                ", signatureType=" + signatureType +
                ", offset=" + offset +
                ", length=" + length +
                ", decompressedLength=" + decompressedLength +
                ", manifestId=" + manifestId +
                '}';
    }
}
