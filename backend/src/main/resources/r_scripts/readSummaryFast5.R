resultsFast5 <- list.unserialize(summaryPath)

results <- list(reads2DQualityOutliers = outliersFinder(resultsFast5$readsQualityMulti$q_2D))
results <- list.append(results, readsTemplateQualityOutliers = outliersFinder(resultsFast5$readsQualityMulti$q_template))
results <- list.append(results, readsComplementQualityOutliers = outliersFinder(resultsFast5$readsQualityMulti$q_complement))