# id, start_time, duration, num_events, medial_signal
# id, czas start, czas trwania, liczba zdarzen, sredni sygnal

out <- eventRate(summaryData)

temp <- select(out, id)
id <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, start_time)
startTime <- matrix(as.charackter(unlist(temp)), nrow = nrow(temp))
temp <- select(out, duration)
duration <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, num_events)
duration <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, medial_signal)
startTime <- matrix(as.charackter(unlist(temp)), nrow = nrow(temp))