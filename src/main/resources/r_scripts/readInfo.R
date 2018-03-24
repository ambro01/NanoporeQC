# id, file, read, channel, mux, pass

out <- tryCatch(readInfo(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, id), error = function(cond){return (tibble(id = numeric()))})
id <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, file), error = function(cond){return (tibble(file = character()))})
file <- matrix(as.character(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, read), error = function(cond){return (tibble(read = numeric()))})
read <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, channel), error = function(cond){return (tibble(channel = numeric()))})
channel <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, mux), error = function(cond){return (tibble(mux = numeric()))})
mux <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, pass), error = function(cond){return (tibble(pass = logical()))})
pass <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))
