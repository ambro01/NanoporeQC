# id, start_time, duration, num_events, median_signal
# id, czas start, czas trwania, liczba zdarzen, sredni sygnal

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
