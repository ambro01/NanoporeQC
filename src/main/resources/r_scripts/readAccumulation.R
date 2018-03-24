# minute, new_reads, accumulation
# przyrost ilosci odczytow w czasie

#' Plot the accumulation of reads over the duration of the experiment.
#' @param summaryData Object of class \linkS4class{Fast5Summary}.
#' @return Returns an object of class \code{gg} representing the plot.
#' @examples
#' if( require(minionSummaryData) ) {
#'    data(s.typhi.rep2, package = 'minionSummaryData')
#'    plotReadAccumulation( s.typhi.rep2 )
#' }
#' @export
#' @importFrom dplyr group_by summarise mutate order_by with_order n
readAccumulation <- function(summaryData) {
    readAccumulation <- dplyr::group_by(eventData(summaryData), minute = start_time %/% 60) %>%
        dplyr::summarise(new_reads = n()) %>%
        dplyr::mutate(accumulation = dplyr::order_by(minute, cumsum(new_reads)))
}

out <- tryCatch(readAccumulation(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, minute), error = function(cond){return (tibble(minute = double()))})
minute <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, new_reads), error = function(cond){return (tibble(new_reads = numeric()))})
new_reads <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, accumulation), error = function(cond){return (tibble(accumulation = numeric()))})
accumulation <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))