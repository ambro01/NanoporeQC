# minute, n
# liczba aktywnych kanalow w czasie

#' Plot the number of active channels for each minute of run time
#' @param summaryData Object of class \linkS4class{Fast5Summary}.
#' @return Returns an object of class \code{gg} representing the plot.
#' @examples
#' if( require(minionSummaryData) ) {
#'    data(s.typhi.rep2, package = 'minionSummaryData')
#'    plotActiveChannels( s.typhi.rep2 )
#' }
#' @export
#' @importFrom dplyr mutate count data_frame

activeChannels <- function(summaryData) {
    startEndSummary <- dplyr::mutate(eventData(summaryData), first = start_time %/% 60, last = (start_time + duration) %/% 60)
    tab <- dplyr::data_frame(minute = unlist(apply(startEndSummary, 1, function(x) { x['first']:x['last'] }))) %>% dplyr::count(minute)
}

out <- tryCatch(activeChannels(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, minute), error = function(cond){return (tibble(minute = numeric()))})
minute <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, n), error = function(cond){return (tibble(n = numeric()))})
channels <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))