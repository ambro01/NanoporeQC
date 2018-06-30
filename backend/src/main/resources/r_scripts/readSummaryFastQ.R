resultsFastQ <- list.unserialize(summaryPath)

results <- list(basesQualityOutliers = outliersFinder(resultsFastQ$basesQuality$quality))
results <- list.append(readsQualityOutliers = outliersFinder(resultsFastQ$readsQuality$quality))
