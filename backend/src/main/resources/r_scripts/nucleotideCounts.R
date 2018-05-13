df <- qaSummary[["baseCalls"]]

counts <- rowSums(df)
A <- df$A
C <- df$C
G <- df$G
T <- df$T
N <- df$N