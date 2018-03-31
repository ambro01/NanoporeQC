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
    dplyr::count(baseCalled(summaryData), full_2D)[[2,'n']] / 2)

    if ("pass" %in% names(readInfo(summaryData))) {
        pf <- any(dplyr::count(readInfo(summaryData), pass)[['pass']], na.rm = TRUE)
    } else {
        pf <- FALSE
    }
    if (pf) {
        tab <- c(tab, nrow(filter(readInfo(summaryData), pass == TRUE)))
        res <- data_frame(
        category = factor(c('Fast5 File Count', 'Template', 'Complement', 'Full 2D', 'Pass'),
        levels = c('Fast5 File Count', 'Template', 'Complement', 'Full 2D', 'Pass')),
        count = tab)
    } else {
        res <- data_frame(
        category = factor(c('Fast5 File Count', 'Template', 'Complement', 'Full 2D'),
        levels = c('Fast5 File Count', 'Template', 'Complement', 'Full 2D')),
        count = tab)
    }
}

readCategoryQuals <- function(summaryData) {
    fq <- fastq(summaryData)
    readType <- factor(.readtypeFromFASTQ(fq), levels = c('space', 'template', 'complement', '2D'))
    meanBaseQuality <- ShortRead::alphabetScore(Biostrings::quality(fq)) / width(fq)
    res <- data.frame(readType, meanBaseQuality)
}

readTypeProduction <- function(summaryData, groupedMinutes = 10) {
    tmp <- dplyr::left_join(baseCalled(summaryData), readInfo(summaryData), by = 'id') %>%
        filter(strand == "template") %>%
        mutate(time_group = start_time %/% (60 * groupedMinutes)) %>%
        group_by(time_group, full_2D, pass) %>%
        summarise(count = n()) %>%
        mutate(hour = (time_group * groupedMinutes)/60 )
}