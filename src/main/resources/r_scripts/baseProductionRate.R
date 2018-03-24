# id, num_events, duration, start_time, strand, full_2D, bases_called
# id, ilosc zdarzen, czas trwania, czas startu, typ odczytu, czy jest 2D, ilosc dopasowan

#' Plot the mean rate at which bases are recorded
#'
#' For each read, the ratio between the total number of bases called in the read
#' (template and complement strand, but not 2D composite) and the time spent in
#' the pore is calculated.  This is then plotted against the time the read
#' entered the pore, allow us to assess whether the rate at which callable
#' bases are read changes during the experiment run time.
#'
#' This is likely very similar to \code{\link{plotEventRate}}, although one may
#' find that large number of events occur that can not be base called,
#' resulting in a difference between these two plots.
#'
#'
#' @param summaryData Object of class \linkS4class{Fast5Summary}.
#' @return Returns an object of class \code{gg} representing the plot.
#' @examples
#' if( require(minionSummaryData) ) {
#'    data(s.typhi.rep2, package = 'minionSummaryData')
#'    plotBaseProductionRate( s.typhi.rep2 )
#' }
#' @export
#' @importFrom dplyr mutate
#' @importFrom ShortRead width

baseProductionRate <- function(summaryData) {
    ## select only the fastq records for the template and complement reads
    ## ignore the composite 2D reads here
    recordTable <- .matchRecords(summaryData)
    fastqIDX <- c(recordTable[['fastqTemplate']], recordTable[['fastqComplement']])
    fastqIDX <- fastqIDX[-which(is.na(fastqIDX))]
    res <- mutate(baseCalled(summaryData), bases_called = ShortRead::width(fastq(summaryData)[ fastqIDX ]))
}

# id, num_events, duration, start_time, strand, full_2D, bases_called

out <- tryCatch(baseProductionRate(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, id), error = function(cond){return (tibble(id = numeric()))})
id <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, num_events), error = function(cond){return (tibble(num_events = double()))})
num_events <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, duration), error = function(cond){return (tibble(duration = double()))})
duration <- matrix(as.double(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, start_time), error = function(cond){return (tibble(start_time = numeric()))})
start_time <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, strand), error = function(cond){return (tibble(strand = character()))})
strand <- matrix(as.character(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, full_2D), error = function(cond){return (tibble(full_2D = logical()))})
full_2D <- matrix(as.logical(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, bases_called), error = function(cond){return (tibble(bases_called = numeric()))})
bases_called <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))