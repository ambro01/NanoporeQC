#' @importFrom ShortRead id
.idFromFASTQ <- function(fastq_obj) {
    as.integer(do.call(rbind, strsplit(as.character(ShortRead::id(fastq_obj)), "_"))[,1])
}

#' @importFrom ShortRead id
.readtypeFromFASTQ <- function(fastq_obj) {
    do.call(rbind, strsplit(as.character(ShortRead::id(fastq_obj)), "_"))[,2]
}

.matchRecords <- function(summaryData) {
    ids <- readInfo(summaryData)[['id']]
    ri_row <- match(ids, readInfo(summaryData)[['id']])
    ed_row <- match(ids, eventData(summaryData)[['id']])
    bct_row <- match(ids, filter(baseCalled(summaryData), strand == "template")[['id']])
    bcc_row <- match(ids, filter(baseCalled(summaryData), strand == "complement")[['id']]) +
    nrow(filter(baseCalled(summaryData), strand == "template"))

    fastq_id <- .idFromFASTQ( fastq(summaryData) )
    fastq_id <- fastq_id[-(1:nrow(summaryData@baseCalled))]
    fq_row <- match(ids, fastq_id) + nrow(summaryData@baseCalled)

    record_table <- tibble(id = ids,
    readInfo = ri_row,
    eventData = ed_row,
    baseCalledTemplate = bct_row,
    baseCalledComplement = bcc_row,
    fastqTemplate = bct_row,
    fastqComplement = bcc_row,
    fastq2D = fq_row)

    return(record_table)
}

checkIsEmpty <- function(vector) {
    if (length(vector) == 0) {
        return (0)
    } else {
        return (vector)
    }
}