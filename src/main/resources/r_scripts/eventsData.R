# id, start_time, duration, num_events, median_signal
# id, czas start, czas trwania, liczba zdarzen, sredni sygnal

out <- tryCatch(eventData(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, start_time), error = function(cond){return (tibble(start_time = double()))})
start_time <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, duration), error = function(cond){return (tibble(duration = double()))})
duration <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, num_events), error = function(cond){return (tibble(num_events = numeric()))})
num_events <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
