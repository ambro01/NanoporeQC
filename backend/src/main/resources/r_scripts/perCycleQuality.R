df <- qaSummary[["perCycle"]]

df <- df$quality

meanOut <- df %>% group_by(Cycle) %>% summarise_at(.vars = names(.)[3],.funs = c(mean="mean", median="median"))

quantileOut <- df %>% group_by(Cycle) %>% do(data.frame(t(quantile(.$Score, probs = c(0.25, 0.50, 0.75)))))

cycle <- meanOut$Cycle
mean_ <- meanOut$mean
median_ <- meanOut$median

q25 <- quantileOut$X25.
q50 <- quantileOut$X50.
q75 <- quantileOut$X75.