readAccumulation <- group_by(eventData(summaryData), minute = start_time%/%60) %>%
    summarise(new_reads = n()) %>% mutate(accumulation = order_by(minute,
    cumsum(new_reads)))

matrixData <- matrix(as.numeric(unlist(readAccumulation)),nrow=nrow(readAccumulation))

minutes <- matrix(as.numeric(unlist(readAccumulation[1])),nrow=nrow(readAccumulation[1]))
newReads <- matrix(as.numeric(unlist(readAccumulation[2])),nrow=nrow(readAccumulation[2]))
accumulation <- matrix(as.numeric(unlist(readAccumulation[3])),nrow=nrow(readAccumulation[3]))