

out <- tryCatch(kbPerChannel(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, channel), error = function(cond){return (tibble(channel = numeric()))})
channel <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, kb), error = function(cond){return (tibble(kb = numeric()))})
kb <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))