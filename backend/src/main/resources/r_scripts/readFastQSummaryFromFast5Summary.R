fq <- fastq(summaryData)

writeFastq(fq, filePath)
qualitySummary <- readQualityFromShortReadQ(fq)
qaSummary <- qa(dirPath, "*fastq[.gz]?")