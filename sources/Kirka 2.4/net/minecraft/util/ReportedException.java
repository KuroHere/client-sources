/*
 * Decompiled with CFR 0.143.
 */
package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException
extends RuntimeException {
    private final CrashReport theReportedExceptionCrashReport;
    private static final String __OBFID = "CL_00001579";

    public ReportedException(CrashReport p_i1356_1_) {
        this.theReportedExceptionCrashReport = p_i1356_1_;
    }

    public CrashReport getCrashReport() {
        return this.theReportedExceptionCrashReport;
    }

    @Override
    public Throwable getCause() {
        return this.theReportedExceptionCrashReport.getCrashCause();
    }

    @Override
    public String getMessage() {
        return this.theReportedExceptionCrashReport.getDescription();
    }
}

