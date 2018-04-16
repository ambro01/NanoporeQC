# category, count
# ilosc nukleotydow w zaleznosci od typu odczytu

#' Plot the proportion of template, complement and 2D reads found a dataset.
#'
#' Generates a bar plot showing the breakdown of read types found in a set of fast5 files.  There is a strict hierarchy to the types of read that can be found in a fast5 file.  A full 2D read requires both a complement and template strand to have been read correctly.  Similarly, a complement strand can only be present if the template was read successfully.  Finally, you can encounter a file containing now called bases on either strand.
#' Here we visualise the total number of fast5 files, along with the counts containing each of the categories above.  For an ideal dataset all four bars will be the same height.  This is unlikely, but the drop between bars can give some indication of data quality.
#' @param summaryData Object of class \linkS4class{Fast5Summary}.
#' @return Returns an object of class \code{gg} representing the plot.
#' @examples
#' if( require(minionSummaryData) ) {
#'    data(s.typhi.rep2, package = 'minionSummaryData')
#'    plotReadCategoryCounts( s.typhi.rep2 )
#' }
#' @export
#' @importFrom dplyr summarise count

out <- tryCatch(readCategoryCounts(summaryData), error = function(cond){return (tibble())})

temp <- tryCatch(select(out, category), error = function(cond){return (tibble(category = character()))})
category <- matrix(as.character(unlist(temp)), nrow = nrow(temp))

temp <- tryCatch(select(out, count), error = function(cond){return (tibble(count = numeric()))})
count <- matrix(as.numeric(unlist(temp)), nrow = nrow(temp))

files_count <- count[1,1]
template_count <- count[2,1]
complement_count <- count[3,1]
full_2d_count <- count[4,1]