activeChannels <- function(summaryData) {
    startEndSummary <- dplyr::mutate(eventData(summaryData), first = start_time %/% 60, last = (start_time + duration) %/% 60)
    tab <- dplyr::data_frame(minute = unlist(apply(startEndSummary, 1, function(x) { x['first']:x['last'] }))) %>% dplyr::count(minute)
}

baseProductionRate <- function(summaryData) {
    ## select only the fastq records for the template and complement reads
    ## ignore the composite 2D reads here
    recordTable <- .matchRecords(summaryData)
    fastqIDX <- c(recordTable[['fastqTemplate']], recordTable[['fastqComplement']])
    fastqIDX <- fastqIDX[-which(is.na(fastqIDX))]
    res <- mutate(baseCalled(summaryData), bases_called = ShortRead::width(fastq(summaryData)[ fastqIDX ]))
}

readAccumulation <- function(summaryData) {
    readAccumulation <- dplyr::group_by(eventData(summaryData), minute = start_time %/% 60) %>%
        dplyr::summarise(new_reads = n()) %>%
        dplyr::mutate(accumulation = dplyr::order_by(minute, cumsum(new_reads)))
}

readCategoryCounts <- function(summaryData) {
    tab <- c(nrow(readInfo(summaryData)),
    dplyr::count(baseCalled(summaryData), strand, sort = TRUE)[['n']],
    nrow(subset(baseCalled(summaryData), full_2D == TRUE)) / 2)

    res <- data_frame(category = c('Fast5 File', 'Template', 'Complement', 'Full 2D'), count = tab)
}

readCategoryQuals <- function(summaryData) {
    fq <- fastq(summaryData)
    readType <- factor(.readtypeFromFASTQ(fq), levels = c('space', 'template', 'complement', '2D'))
    meanBaseQuality <- ShortRead::alphabetScore(Biostrings::quality(fq)) / ShortRead::width(fq)
    res <- data.frame(readType, meanBaseQuality)
    minRes <- aggregate(meanBaseQuality ~ readType, res, function(x) min(x))
    maxRes <- aggregate(meanBaseQuality ~ readType, res, function(x) max(x))
    meanRes <- aggregate(meanBaseQuality ~ readType, res, function(x) mean(x))
    medianRes <- aggregate(meanBaseQuality ~ readType, res, function(x) median(x))

    readType <- tryCatch(select(minRes, readType), error = function(cond){return (tibble(readType = character()))})
    min_ <- tryCatch(select(minRes, meanBaseQuality), error = function(cond){return (tibble(min = numeric()))})
    max_ <- tryCatch(select(maxRes, meanBaseQuality), error = function(cond){return (tibble(max = numeric()))})
    mean_ <- tryCatch(select(meanRes, meanBaseQuality), error = function(cond){return (tibble(mean = numeric()))})
    median_ <- tryCatch(select(medianRes, meanBaseQuality), error = function(cond){return (tibble(median = numeric()))})

    res <- data.frame(readType, min_, max_, mean_, median_)
    colnames(res) <- c('category', 'min', 'max', 'mean', 'median')
    return (res)
}

readTypeProduction <- function(summaryData, groupedMinutes = 10) {
    tmp <- dplyr::left_join(baseCalled(summaryData), readInfo(summaryData), by = 'id') %>%
        filter(strand == "template") %>%
        dplyr::mutate(time_group = start_time %/% (60 * groupedMinutes)) %>%
        dplyr::group_by(time_group, full_2D, pass) %>%
        dplyr::summarise(count = n()) %>%
        dplyr::mutate(hour = (time_group * groupedMinutes)/60 )
}

kbPerChannel <- function(summaryData) {
    tmp <- dplyr::mutate(baseCalled(summaryData), seq_length = BiocGenerics::width(fastq(summaryData)[1:nrow(baseCalled(summaryData))]),
    channel = readInfo(summaryData)[['channel']][ match(baseCalled(summaryData)[['id']],
    readInfo(summaryData)[['id']]) ]) %>% dplyr::group_by(channel) %>% dplyr::summarise(kb = sum(seq_length) / 1000)
}

readsPerChannel <- function(summaryData) {
    tmp <- dplyr::group_by(readInfo(summaryData), channel) %>% dplyr::summarise(nreads = n())
}