# id, num_events, duration, start_time, strand, full_2D

out <- tryCatch(baseCalled(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, id), error = function(cond){return (tibble(id = numeric()))})
id <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, num_events), error = function(cond){return (tibble(num_events = numeric()))})
num_events <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, duration), error = function(cond){return (tibble(duration = double()))})
duration <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, start_time), error = function(cond){return (tibble(start_time = double()))})
start_time <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, strand), error = function(cond){return (tibble(strand = character()))})
strand <- matrix(as.character(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, full_2D), error = function(cond){return (tibble(full_2D = logical()))})
full_2D <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))

templateResult <- out %>% filter(strand == "template")

complementResult <- out %>% filter(strand == "complement")