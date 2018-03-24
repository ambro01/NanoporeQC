# time_group, full_2D, pass, count, hour

#' Plots the median recorded current for each fast5 file against the time at
#' which the recording began.
#'
#' @param summaryData Object of class \linkS4class{Fast5Summary}.
#' @param groupedMinutes Integer specifying how many minutes of runtime should be grouped together.
#' @return Returns an object of class \code{gg} representing the plot.
#' @examples
#' if( require(minionSummaryData) ) {
#'    data(s.typhi.rep2, package = 'minionSummaryData')
#'    plotReadTypeProduction( s.typhi.rep2 )
#' }
#' @export
#' @importFrom dplyr left_join
readTypeProduction <- function(summaryData, groupedMinutes = 10) {
    tmp <- dplyr::left_join(baseCalled(summaryData), readInfo(summaryData), by = 'id') %>%
        filter(strand == "template") %>%
        mutate(time_group = start_time %/% (60 * groupedMinutes)) %>%
        group_by(time_group, full_2D, pass) %>%
        summarise(count = n()) %>%
        mutate(hour = (time_group * groupedMinutes)/60 )
}

out <- tryCatch(readTypeProduction(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, time_group), error = function(cond){return (tibble(pass = double()))})
time_group <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, full_2D), error = function(cond){return (tibble(pass = logical()))})
full_2D <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, pass), error = function(cond){return (tibble(pass = logical()))})
pass <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, count), error = function(cond){return (tibble(pass = numeric()))})
count <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, hour), error = function(cond){return (tibble(pass = double()))})
hour <- matrix(as.double(unlist(temp)), nrow = nrow(temp))