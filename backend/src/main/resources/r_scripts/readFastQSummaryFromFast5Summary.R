fq <- fastq(summaryData)

writeFastq(fq, filePath)

qaSummary <- qa(dirPath, "*fastq[.gz]?")