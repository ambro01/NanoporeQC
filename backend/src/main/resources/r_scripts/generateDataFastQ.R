#Nucleodites counts
df <- qaSummary[["baseCalls"]]
counts <- rowSums(df)
A <- df$A
C <- df$C
G <- df$G
T <- df$T
N <- df$N
counts <- A+C+G+T+N
resultsFastQ <- list(nucleotidesCounts = list(counts=counts, A=A, C=C, G=G, T=T, N=N))

# Reads quality
quality <- qualitySummary$meanBaseQuality
id <- seq(1, length(quality))
quantileOut <- quantile(quality, probs = c(0.25, 0.50, 0.75), na.rm = TRUE, names = FALSE)
mean <- mean(quality, na.rm = TRUE)
median <- median(quality, na.rm = TRUE)
q25 <- quantileOut[1]
q50 <- quantileOut[2]
q75 <- quantileOut[3]
resultsFastQ <- list.append(resultsFastQ, readsQuality = list(id=id, quality=quality, mean=mean, median=median, q25=q25, q50=q50, q75=q75))

# Quality denisty
density_quality <- density(quality)
quality <- density_quality$x
density <- density_quality$y
resultsFastQ <- list.append(resultsFastQ, readsQualityDensity = list(quality=quality, density=density))

# Base quality
d <- tryCatch(as(quality(fq), "matrix"), error = function(cond){return (matrix())})
quality <- colMeans(d, na.rm = TRUE, dims = 1)
id <- seq(1, length(quality))
quantileOut <- quantile(quality, probs = c(0.25, 0.50, 0.75), na.rm = TRUE, names = FALSE)
mean <- mean(quality, na.rm = TRUE)
median <- median(quality, na.rm = TRUE)
q25 <- quantileOut[1]
q50 <- quantileOut[2]
q75 <- quantileOut[3]

resultsFastQ <- list.append(resultsFastQ, basesQuality = list(id=id, quality=quality, mean=mean, median=median, q25=q25, q50=q50, q75=q75))

# Quality denisty
density_quality <- density(quality)
quality <- density_quality$x
density <- density_quality$y
resultsFastQ <- list.append(resultsFastQ, basesQualityDensity = list(quality=quality, density=density))

# Base call
d <- tryCatch(as(sread(fq), "matrix"), error = function(cond){return (matrix())})
id <- seq(1, ncol(d))
A <- colSums(d == 'A', na.rm = TRUE, dims = 1)
T <- colSums(d == 'T', na.rm = TRUE, dims = 1)
C <- colSums(d == 'C', na.rm = TRUE, dims = 1)
G <- colSums(d == 'G', na.rm = TRUE, dims = 1)
N <- colSums(d == 'N', na.rm = TRUE, dims = 1)
resultsFastQ <- list.append(resultsFastQ, basesCalls = list(id=id, A=A, T=T, C=C, G=G, N=N))

# Base CG content
all <- A+T+C+G+N
cg <- C+G
cgPart <- cg/all
resultsFastQ <- list.append(resultsFastQ, basesCgContent = list(id=id, cgContent=cgPart))

# Reads distribution
df <- qaSummary[["sequenceDistribution"]]
occurrences <- df$nOccurrences
reads <- df$nReads
file_name <- df$lane
resultsFastQ <- list.append(resultsFastQ, sequencesDistribution = list(occurrences=occurrences, reads=reads, fileName=file_name))

# Duplicated reads
df <- ShortRead:::.freqSequences(qaSummary, "read")
sequence <- df$sequence
count <- df$count
resultsFastQ <- list.append(resultsFastQ, duplicatedSequences = list(sequence=sequence, count=count))