# id, file, read, channel, mux, pass

out <- readInfo(summaryData)

temp <- select(out, id)
id <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, file)
file <- matrix(as.character(unlist(temp)), nrow = nrow(temp))
temp <- select(out, read)
read <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, channel)
chnnel <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, mux)
mux <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, pass)
pass <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))
