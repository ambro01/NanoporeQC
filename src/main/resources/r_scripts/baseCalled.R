# id, num_events, duration, start_time, strand, full_2D

out <- baseCalled(summaryData)

temp <- select(out, id)
id <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, num_events)
numEvents <- matrix(as.character(unlist(temp)), nrow = nrow(temp))
temp <- select(out, duration)
duration <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, start_time)
startTime <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, strand)
strand <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, full_2D)
isFull2D <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))
