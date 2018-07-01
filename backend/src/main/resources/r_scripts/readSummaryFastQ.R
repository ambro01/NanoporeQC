resultsFastQ <- list.unserialize(summaryPath)

resultsFastQNotSaved <- list(basesQualityOutliers = outliersFinder(resultsFastQ$basesQuality$quality))
resultsFastQNotSaved <- list.append(resultsFastQNotSaved, readsQualityOutliers = outliersFinder(resultsFastQ$readsQuality$quality))