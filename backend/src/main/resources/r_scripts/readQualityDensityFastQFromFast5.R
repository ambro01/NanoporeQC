
out <- tryCatch(qualitySummary, error = function(cond){return (tibble())})

q_template <- (out %>% filter(readType == "template"))$meanBaseQuality
q_complement <- (out %>% filter(readType == "complement"))$meanBaseQuality
q_2D <- (out %>% filter(readType == "2D"))$meanBaseQuality


d_template <- density(q_template)
d_complement <- density(q_complement)
d_2D <- density(q_2D)

quality_template <- d_template$x[seq(1, length(d_template$x), 4)]
density_template <- d_template$y[seq(1, length(d_template$y), 4)]
quality_complement <- d_complement$x[seq(1, length(d_complement$x), 4)]
density_complement <- d_complement$y[seq(1, length(d_complement$y), 4)]
quality_2D <- d_2D$x[seq(1, length(d_2D$x), 4)]
density_2D <- d_2D$y[seq(1, length(d_2D$y), 4)]
