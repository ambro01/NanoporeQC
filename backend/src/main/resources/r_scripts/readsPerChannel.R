
out <- tryCatch(readsPerChannel(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, channel), error = function(cond){return (tibble(channel = numeric()))})
channel <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, nreads), error = function(cond){return (tibble(nreads = numeric()))})
reads <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))