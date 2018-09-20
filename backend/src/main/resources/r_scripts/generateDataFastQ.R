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
quality <- data.frame(meanBaseQuality=ShortRead::alphabetScore(Biostrings::quality(fq)) / ShortRead::width(fq))
quality <- quality$meanBaseQuality
id <- seq(1, length(quality))
quantileOut <- quantile(quality, probs = c(0.25, 0.50, 0.75), na.rm = TRUE, names = FALSE)
mean <- mean(quality, na.rm = TRUE)
median <- median(quality, na.rm = TRUE)
q25 <- quantileOut[1]
q50 <- quantileOut[2]
q75 <- quantileOut[3]
resultsFastQ <- list.append(resultsFastQ, readsQuality = list(id=id, quality=quality, mean=mean, median=median, q25=q25, q50=q50, q75=q75))

# Outliers of reads quality
resultsFastQNotSaved <- list(readsQualityOutliers = outliersFinder(quality))

# Reads quality denisty
density_quality <- tryCatch(density(quality), error = function(cond){return (tibble())})

quality <- if(length(density_quality) > 0){density_quality$x} else {list()}
density <- if(length(density_quality) > 0){density_quality$y} else {list()}
resultsFastQ <- list.append(resultsFastQ, readsQualityDensity = list(quality=quality, density=density))

# Base quality
df <- qaSummary[["perCycle"]]
df <- df$quality

df <- df %>% mutate(ScoreCount = Score * Count)
score <- df %>% group_by(Cycle) %>% summarise_at(.vars = names(.)[6],.funs = c(Score="sum"))
count <- df %>% group_by(Cycle) %>% summarise_at(.vars = names(.)[4],.funs = c(Count="sum"))
df <- merge(score, count, by = "Cycle")
df <- df %>% mutate(meanQuality = Score/Count)
id <- df$Cycle
quality <- df$meanQuality

resultsFastQ <- list.append(resultsFastQ, basesQuality = list(id=id, quality=quality))

# Outliers of bases quality
resultsFastQNotSaved <- list.append(resultsFastQNotSaved, basesQualityOutliers = outliersFinder(quality))

# Checking qaulity status
df <- qaSummary[["perCycle"]]
quality <- df$quality
quantiles <- quality %>% group_by(Cycle) %>% do(data.frame(t(quantile(.$Score, c(0.25, 0.5)))))

status <- 'Success'
if (any(quantiles$X50.) < 10 || any(quantiles$X25.) < 5) {
    status <- 'Warning'
}
if (any(quantiles$X50.) < 8 || any(quantiles$X25.) < 3) {
    status <- 'Failure'
}
resultsFastQ <- list.append(resultsFastQ, basesQualityStatus = list(status=status))


# Base quality denisty
density_quality <- tryCatch(density(quality[rep(row.names(quality), df$Count), 3]), error = function(cond){return (tibble())})

quality <- if(length(density_quality) > 0){density_quality$x} else {list()}
density <- if(length(density_quality) > 0){density_quality$y} else {list()}

resultsFastQ <- list.append(resultsFastQ, basesQualityDensity = list(quality=quality, density=density))

# Base call
df <- df$baseCall

df <- df %>% group_by(Cycle, Base) %>% summarise_at(.vars = names(.)[3],.funs = c(Count="sum"))

A <- subset(df %>% filter(Base == "A"), select = c("Cycle", "Count"))
colnames(A)[2] <- "Count_A"

C <- subset(df %>% filter(Base == "C"), select = c("Cycle", "Count"))
colnames(C)[2] <- "Count_C"

G <- subset(df %>% filter(Base == "G"), select = c("Cycle", "Count"))
colnames(G)[2] <- "Count_G"

T <- subset(df %>% filter(Base == "T"), select = c("Cycle", "Count"))
colnames(T)[2] <- "Count_T"

N <- subset(df %>% filter(Base == "N" || Base == "-"), select = c("Cycle", "Count"))
colnames(N)[2] <- "Count_N"

out <- merge(x = A, y = C, by="Cycle", all.x = TRUE)
out <- merge(x = out, y = G, by="Cycle", all.x = TRUE)
out <- merge(x = out, y = T, by="Cycle", all.x = TRUE)
out <- merge(x = out, y = N, by="Cycle", all.x = TRUE)

out[is.na(out)] <- 0

id <- out$Cycle
A <- as.double(out$Count_A)
C <- as.double(out$Count_C)
G <- as.double(out$Count_G)
T <- as.double(out$Count_T)
N <- out$count_N

if (is.null(N) || is.na(N)) {
    N = 0
}

resultsFastQ <- list.append(resultsFastQ, basesCalls = list(id=id, A=A, T=T, C=C, G=G, N=N))

# Base CG content
all <- A+T+C+G+N
cg <- C+G
cgPart <- cg/all
resultsFastQ <- list.append(resultsFastQ, basesCgContent = list(id=id, cgContent=cgPart))

# Base CG density
density_cg <- tryCatch(density(cgPart), error = function(cond){return (tibble())})
cgPart <- if(length(density_cg) > 0){density_cg$x} else {list()}
density <- if(length(density_cg) > 0){density_cg$y} else {list()}

resultsFastQ <- list.append(resultsFastQ, basesCgDensity = list(cgContent=cgPart, density=density))

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

# Prepare data for clustering and outliers detection
resultsFastQ <- list.append(resultsFastQ, clusteringData = getDataForClustering(fq))