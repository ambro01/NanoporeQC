# id, num_events, duration, start_time, strand, full_2D

out <- tryCatch(readQuality(summaryData), error = function(cond){return (tibble())})

length_ <- length(out)

quality <- out
min_ <- tryCatch(rep(min(out), length_), error = function(cond){return (tibble(min_ = double()))})
max_ <- tryCatch(rep(max(out), length_), error = function(cond){return (tibble(max_ = double()))})
mean_ <- tryCatch(rep(mean(out), length_), error = function(cond){return (tibble(mean_ = double()))})
median_ <- tryCatch(rep(median(out), length_), error = function(cond){return (tibble(median_ = double()))})

id <- seq(1, length_)
