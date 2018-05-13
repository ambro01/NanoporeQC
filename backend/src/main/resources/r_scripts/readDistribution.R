df <- qaSummary[["sequenceDistribution"]]

cumulative <- tapply(df$nOccurrences * df$nReads, df$lane,
function(elt) {
    cs <- cumsum(elt)
    (cs - cs[1] + 1)/(diff(range(cs)) + 1L)
})

occurrences <- log10(df$nOccurrences)