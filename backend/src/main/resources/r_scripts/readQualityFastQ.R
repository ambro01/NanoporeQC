
out <- tryCatch(qualitySummary, error = function(cond){return (tibble())})

quality <- out$meanBaseQuality

id <- seq(1, length(quality))
