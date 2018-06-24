fq <- readFastq(dirPath, "*fastq[.gz]?")
qualitySummary <- readQualityFromShortReadQ(fq)
qaSummary <- qa(dirPath, "*fastq[.gz]?")

