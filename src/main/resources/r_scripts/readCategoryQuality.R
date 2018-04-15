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

out <- tryCatch(readCategoryQuals(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, category), error = function(cond){return (tibble(category = character()))})
category <- matrix(as.character(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, min), error = function(cond){return (tibble(min = numeric()))})
min <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, max), error = function(cond){return (tibble(max = numeric()))})
max <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, mean), error = function(cond){return (tibble(mean = numeric()))})
mean <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, median), error = function(cond){return (tibble(median = numeric()))})
median <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

