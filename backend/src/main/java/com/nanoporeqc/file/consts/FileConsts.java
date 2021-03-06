package com.nanoporeqc.file.consts;

public class FileConsts {
    private static final String BASE_DIR = "/usr/nanoporeqc/";
    public static final String FILES_DIR = BASE_DIR + "files/";
    public static final String SCRIPTS_DIR = BASE_DIR + "scripts/";
    public static final String SUMMARY_DIR = BASE_DIR + "summary/";
    public static final String SUMMARY_FAST5_FILE = SUMMARY_DIR + "fast5.json";
    public static final String SUMMARY_FASTQ_FILE = SUMMARY_DIR + "fastq.json";
    public static final String FASTQ_FILE_FROM_FAST5 = FILES_DIR + "fromFast5.fastq";
    public static final String REPORT_DIR = BASE_DIR + "report/";

    public static final String SCRIPTS_RESOURCE = "/r_scripts/";

}