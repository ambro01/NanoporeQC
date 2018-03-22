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

out <- activeChannels(summaryData)

matrixData <- matrix(as.numeric(unlist(out)), nrow = nrow(out))

temp <- select(out, minute)
minutes <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))
temp <- select(out, n)
channels <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))