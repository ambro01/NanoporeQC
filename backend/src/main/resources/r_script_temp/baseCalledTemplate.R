# id, num_events, duration, start_time, strand, full_2D

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