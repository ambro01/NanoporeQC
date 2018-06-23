
out <- tryCatch(readQuality(summaryData), error = function(cond){return (tibble())})

q_template <- (out %>% filter(readType == "template"))$meanBaseQuality
q_complement <- (out %>% filter(readType == "complement"))$meanBaseQuality
q_2D <- (out %>% filter(readType == "2D"))$meanBaseQuality

id <- seq(1, length(q_template))
