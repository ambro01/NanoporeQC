resultsFast5 <- list.unserialize(summaryPath)

resultsFast5NotSaved <- list(reads2DQualityOutliers = outliersFinder(resultsFast5$readsQualityMulti$q_2D))
resultsFast5NotSaved <- list.append(resultsFast5NotSaved, readsTemplateQualityOutliers = outliersFinder(resultsFast5$readsQualityMulti$q_template))
resultsFast5NotSaved <- list.append(resultsFast5NotSaved, readsComplementQualityOutliers = outliersFinder(resultsFast5$readsQualityMulti$q_complement))