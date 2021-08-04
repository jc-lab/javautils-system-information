package kr.jclab.javautils.systeminformation.smbios;

import kr.jclab.javautils.systeminformation.model.SmbiosInformation;

public interface DmiParsable<T extends SmbiosInformation> {
    int getDmiType();

    /**
     * Parse and Merge
     * @param data dmi data
     * @param old old object
     * @return merged object (merge to the old object)
     */
    T parse(DMIData data, SmbiosInformation old);
}
