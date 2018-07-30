fq <- fastq(summaryData)
# Reads accumulation
out <- tryCatch(readAccumulation(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, minute), error = function(cond){return (tibble(minute = double()))})
minute <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, new_reads), error = function(cond){return (tibble(new_reads = numeric()))})
newReads <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, accumulation), error = function(cond){return (tibble(accumulation = numeric()))})
accumulation <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

resultsFast5 <- list(readsAccumulation = list(minute=minute, newReads=newReads, accumulation=accumulation))

# Active channels
out <- tryCatch(activeChannels(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, minute), error = function(cond){return (tibble(minute = numeric()))})
minute <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, n), error = function(cond){return (tibble(n = numeric()))})
channels <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

resultsFast5 <- list.append(resultsFast5, activeChannels = list(minute=minute, channels=channels))

# Reads quality
out <- tryCatch(readQuality(fq), error = function(cond){return (tibble())})
q_template <- (out %>% filter(readType == "template"))$meanBaseQuality
q_complement <- (out %>% filter(readType == "complement"))$meanBaseQuality
q_2D <- (out %>% filter(readType == "2D"))$meanBaseQuality
id <- seq(1, length(q_template))

resultsFast5 <- list.append(resultsFast5, readsQualityMulti = list(id=id, q_template=q_template, q_complement=q_complement, q_2D=q_2D))

# Outliers of reads quality
resultsFast5NotSaved <- list(reads2DQualityOutliers=outliersFinder(q_2D))
resultsFast5NotSaved <- list.append(resultsFast5NotSaved, readsTemplateQualityOutliers=outliersFinder(q_template))
resultsFast5NotSaved <- list.append(resultsFast5NotSaved, readsComplementQualityOutliers=outliersFinder(q_complement))

# Checking qaulity status
quantile25 <- quantile(q_2D, 0.25, na.rm = TRUE, names = FALSE)
median <- median(q_2D, na.rm = TRUE)
status <- 'Success'
if (median < 15 || quantile25 < 5) {
    status <- 'Warning'
}
if (median < 10 || quantile25 < 3) {
    status <- 'Failure'
}
resultsFast5 <- list.append(resultsFast5, readsQualityStatus = list(status=status))

# Reads quality density
out <- tryCatch(readQuality(fq), error = function(cond){return (tibble())})
q_template <- (out %>% filter(readType == "template"))$meanBaseQuality
q_complement <- (out %>% filter(readType == "complement"))$meanBaseQuality
q_2D <- (out %>% filter(readType == "2D"))$meanBaseQuality
d_template <- tryCatch(density(q_template), error = function(cond){return (tibble())})
d_complement <- tryCatch(density(q_complement), error = function(cond){return (tibble())})
d_2D <- tryCatch(density(q_2D), error = function(cond){return (tibble())})
quality_template <- if(length(d_template) > 0){d_template$x[seq(1, length(d_template$x), 4)]} else {list()}
density_template <- if(length(d_template) > 0){d_template$y[seq(1, length(d_template$y), 4)]} else {list()}
quality_complement <- if(length(d_complement) > 0){ d_complement$x[seq(1, length(d_complement$x), 4)]} else {list()}
density_complement <- if(length(d_complement) > 0){d_complement$y[seq(1, length(d_complement$y), 4)]} else {list()}
quality_2D <- if(length(d_2D) > 0){d_2D$x[seq(1, length(d_2D$x), 4)]} else {list()}
density_2D <- if(length(d_2D) > 0){d_2D$y[seq(1, length(d_2D$y), 4)]} else {list()}

resultsFast5 <- list.append(resultsFast5, readsQualityDensityMulti = list(quality_template=quality_template, density_template=density_template,
quality_complement=quality_complement, density_complement=density_complement, quality_2D=quality_2D, density_2D=density_2D))

# Reads quality factors
out <- tryCatch(readCategoryQuals(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, category), error = function(cond){return (tibble(category = character()))})
category <- matrix(as.character(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, min), error = function(cond){return (tibble(min = double()))})
min <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, max), error = function(cond){return (tibble(max = double()))})
max <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, mean), error = function(cond){return (tibble(mean = double()))})
mean <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, median), error = function(cond){return (tibble(median = double()))})
median <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

resultsFast5 <- list.append(resultsFast5, readsCategoryQuality = list(category=category, min=min,
max=max, mean=mean, median=median))

# Reads category counts
out <- tryCatch(readCategoryCounts(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, category), error = function(cond){return (tibble(category = character()))})
category <- matrix(as.character(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, count), error = function(cond){return (tibble(count = numeric()))})
count <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
files_count <- count[1,1]
template_count <- count[2,1]
complement_count <- count[3,1]
full_2d_count <- count[4,1]

resultsFast5 <- list.append(resultsFast5, readsCategoryCounts = list(category=category, count=count,
files_count=files_count, template_count=template_count, complement_count=complement_count, full_2d_count=full_2d_count))

# Events counts
out <- tryCatch(eventData(summaryData), error = function(cond){return (tibble())})
out$end_time <- out$start_time + out$duration
a <- data.frame(out$start_time, out$num_events)
b <- data.frame(out$end_time, out$num_events)
colnames(a) <- c('time', 'num_events')
colnames(b) <- c('time', 'num_events')
total <- rbind(a, b)
total[with(total, order(time)),]
temp <- tryCatch(select(out, start_time), error = function(cond){return (tibble(start_time = double()))})
time <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, num_events), error = function(cond){return (tibble(num_events = numeric()))})
events <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

resultsFast5 <- list.append(resultsFast5, eventsCounts = list(time=time, events=events))

# Reads per channel
out <- tryCatch(readsPerChannel(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, channel), error = function(cond){return (tibble(channel = numeric()))})
channel <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, nreads), error = function(cond){return (tibble(nreads = numeric()))})
reads <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

resultsFast5 <- list.append(resultsFast5, readsPerChannel = list(channel=channel, reads=reads))

# Kilobases per channel
out <- tryCatch(kbPerChannel(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, channel), error = function(cond){return (tibble(channel = numeric()))})
channel <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, kb), error = function(cond){return (tibble(kb = numeric()))})
kb <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

resultsFast5 <- list.append(resultsFast5, kbPerChannel = list(channel=channel, kb=kb))

# Summary info
out <- tryCatch(readInfo(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, id), error = function(cond){return (tibble(id = numeric()))})
id <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, file), error = function(cond){return (tibble(file = character()))})
file <- matrix(as.character(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, read), error = function(cond){return (tibble(read = numeric()))})
read <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, channel), error = function(cond){return (tibble(channel = numeric()))})
channel <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

#EventsData
out <- tryCatch(eventData(summaryData), error = function(cond){return (tibble())})
temp <- tryCatch(select(out, start_time), error = function(cond){return (tibble(start_time = double()))})
start_time <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, duration), error = function(cond){return (tibble(duration = double()))})
duration <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, num_events), error = function(cond){return (tibble(num_events = numeric()))})
num_events <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

# basesCalledTemplate
all_out <- tryCatch(baseCalled(summaryData), error = function(cond){return (tibble())})
out <- all_out %>% filter(strand == "template")
temp <- tryCatch(select(out, id), error = function(cond){return (tibble(id = numeric()))})
id_t <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, num_events), error = function(cond){return (tibble(num_events = numeric()))})
num_events_t <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, duration), error = function(cond){return (tibble(duration = double()))})
duration_t <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, start_time), error = function(cond){return (tibble(start_time = double()))})
start_time_t <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, strand), error = function(cond){return (tibble(strand = character()))})
strand_t <- matrix(as.character(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, full_2D), error = function(cond){return (tibble(full_2D = logical()))})
full_2D_t <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))

#basesCalledTemplate
all_out <- tryCatch(baseCalled(summaryData), error = function(cond){return (tibble())})
out <- all_out %>% filter(strand == "complement")
temp <- tryCatch(select(out, id), error = function(cond){return (tibble(id = numeric()))})
id_c <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, num_events), error = function(cond){return (tibble(num_events = numeric()))})
num_events_c <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, duration), error = function(cond){return (tibble(duration = double()))})
duration_c <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, start_time), error = function(cond){return (tibble(start_time = double()))})
start_time_c <- matrix(as.double(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, strand), error = function(cond){return (tibble(strand = character()))})
strand_c <- matrix(as.character(unlist(temp)), nrow = nrow(temp))
temp <- tryCatch(select(out, full_2D), error = function(cond){return (tibble(full_2D = logical()))})
full_2D_c <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))

resultsFast5 <- list.append(resultsFast5, summaryInfo = list(id=id, file=file, read=read, channel=channel, start_time=start_time, duration=duration, num_events=num_events,
num_events_t=num_events_t, duration_t=duration_t, start_time_t=start_time_t, strand_t=strand_t, full_2D_t=full_2D_t,
num_events_c=num_events_c, duration_c=duration_c, start_time_c=start_time_c, strand_c=strand_c, full_2D_c=full_2D_c))

# resultsFast5 <- list.append(resultsFast5, clusteringData = getDataForClustering(fq))