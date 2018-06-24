df <- qaSummary[["perCycle"]]

df <- df$baseCall

df <- df[df$Base != "N" & df$Base != "-", ]
df <- df[order(df$lane, df$Base, df$Cycle), ]

A <- df %>% filter(Base == "A")
A <- subset(A, select = c("Cycle", "Count"))
colnames(A)[2] <- "Count_A"

C <- df %>% filter(Base == "C")
C <- subset(C, select = c("Cycle", "Count"))
colnames(C)[2] <- "Count_C"

G <- df %>% filter(Base == "G")
G <- subset(G, select = c("Cycle", "Count"))
colnames(G)[2] <- "Count_G"

T <- df %>% filter(Base == "T")
T <- subset(T, select = c("Cycle", "Count"))
colnames(T)[2] <- "Count_T"

out <- merge(A, C, by="Cycle")
out <- merge(out, G, by="Cycle")
out <- merge(out, T, by="Cycle")

cycle <- out$Cycle
contentCG <- (out$Count_C + out$Count_G) / (out$Count_A + out$Count_T + out$Count_C + out$Count_G)