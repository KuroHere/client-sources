/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipShort;

public final class GeneralPurposeBit {
    private static final int ENCRYPTION_FLAG = 1;
    private static final int SLIDING_DICTIONARY_SIZE_FLAG = 2;
    private static final int NUMBER_OF_SHANNON_FANO_TREES_FLAG = 4;
    private static final int DATA_DESCRIPTOR_FLAG = 8;
    private static final int STRONG_ENCRYPTION_FLAG = 64;
    public static final int UFT8_NAMES_FLAG = 2048;
    private boolean languageEncodingFlag = false;
    private boolean dataDescriptorFlag = false;
    private boolean encryptionFlag = false;
    private boolean strongEncryptionFlag = false;
    private int slidingDictionarySize;
    private int numberOfShannonFanoTrees;

    public boolean usesUTF8ForNames() {
        return this.languageEncodingFlag;
    }

    public void useUTF8ForNames(boolean bl) {
        this.languageEncodingFlag = bl;
    }

    public boolean usesDataDescriptor() {
        return this.dataDescriptorFlag;
    }

    public void useDataDescriptor(boolean bl) {
        this.dataDescriptorFlag = bl;
    }

    public boolean usesEncryption() {
        return this.encryptionFlag;
    }

    public void useEncryption(boolean bl) {
        this.encryptionFlag = bl;
    }

    public boolean usesStrongEncryption() {
        return this.encryptionFlag && this.strongEncryptionFlag;
    }

    public void useStrongEncryption(boolean bl) {
        this.strongEncryptionFlag = bl;
        if (bl) {
            this.useEncryption(false);
        }
    }

    int getSlidingDictionarySize() {
        return this.slidingDictionarySize;
    }

    int getNumberOfShannonFanoTrees() {
        return this.numberOfShannonFanoTrees;
    }

    public byte[] encode() {
        return ZipShort.getBytes((this.dataDescriptorFlag ? 8 : 0) | (this.languageEncodingFlag ? 2048 : 0) | (this.encryptionFlag ? 1 : 0) | (this.strongEncryptionFlag ? 64 : 0));
    }

    public static GeneralPurposeBit parse(byte[] byArray, int n) {
        int n2 = ZipShort.getValue(byArray, n);
        GeneralPurposeBit generalPurposeBit = new GeneralPurposeBit();
        generalPurposeBit.useDataDescriptor((n2 & 8) != 0);
        generalPurposeBit.useUTF8ForNames((n2 & 0x800) != 0);
        generalPurposeBit.useStrongEncryption((n2 & 0x40) != 0);
        generalPurposeBit.useEncryption((n2 & 1) != 0);
        generalPurposeBit.slidingDictionarySize = (n2 & 2) != 0 ? 8192 : 4096;
        generalPurposeBit.numberOfShannonFanoTrees = (n2 & 4) != 0 ? 3 : 2;
        return generalPurposeBit;
    }

    public int hashCode() {
        return 3 * (7 * (13 * (17 * (this.encryptionFlag ? 1 : 0) + (this.strongEncryptionFlag ? 1 : 0)) + (this.languageEncodingFlag ? 1 : 0)) + (this.dataDescriptorFlag ? 1 : 0));
    }

    public boolean equals(Object object) {
        if (!(object instanceof GeneralPurposeBit)) {
            return true;
        }
        GeneralPurposeBit generalPurposeBit = (GeneralPurposeBit)object;
        return generalPurposeBit.encryptionFlag == this.encryptionFlag && generalPurposeBit.strongEncryptionFlag == this.strongEncryptionFlag && generalPurposeBit.languageEncodingFlag == this.languageEncodingFlag && generalPurposeBit.dataDescriptorFlag == this.dataDescriptorFlag;
    }
}

