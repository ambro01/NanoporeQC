# readType, meanBaseQuality
# srednia jakosc dopasowania (odwzorowania w nukleotydy)

#' Visualise the mean base quality of each read
#'
#' Generates a box plot showing the mean base quality for each read, broken down into the three categories of read type that can be found in a fast5 file.
#'
#' @param summaryData Object of class \linkS4class{Fast5Summary}.
#' @return Returns an object of class \code{gg} representing the plot.
#' @examples
#' if( require(minionSummaryData) ) {
#'    data(s.typhi.rep2, package = 'minionSummaryData')
#'    plotReadCategoryQuals( s.typhi.rep2 )
#' }
#' @export
#' @importFrom ShortRead alphabetScore
#' @importFrom Biostrings quality

readCategoryQuals <- function(summaryData) {
    fq <- fastq(summaryData)
    readType <- factor(.readtypeFromFASTQ(fq), levels = c('space', 'template', 'complement', '2D'))
    meanBaseQuality <- ShortRead::alphabetScore(Biostrings::quality(fq)) / width(fq)
    res <- data.frame(readType, meanBaseQuality)
}

out <- tryCatch(readCategoryQuals(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, read_type), error = function(cond){return (tibble(read_type = character()))})
read_type <- matrix(as.character(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, mean_base_quality), error = function(cond){return (tibble(mean_base_quality = numeric()))})
mean_base_quality <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
