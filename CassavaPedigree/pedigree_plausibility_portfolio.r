##This script is designed for largescale analysis of cassava pedigrees and 
##genotypes retrieved using the SGN cassava breeder database maintained by 
##the Lukas Mueller Lab at Boyce Thompson Institute at Cornell University 
##(https://www.cassavabase.org/).

##The accessions used in this analysis were selected using the CassavaBase
##Wizard with the specifications that all were genotyped using the latest 
##and most detailed genotyping protocol (GBS ApeKI V6) and all have parent lines 
##on record with their genotypes also stored in the database. In all,
##genotypes of 120 accessions and their parents were retrieved for analysis,
##alongside their pedigree records. 

##I eventually created a Perl script in order to run the analysis 
##on all records contained in the database (a 4 day process even running
##on a remote production server!), due to the fact that CassavaBase was built 
##with Perl and the infrastructure already existed for direct database 
##connections. However, this R script is the heart of the project, and
##truly reflects how I as a programmer approached the dataset, rather than 
##the Perl script which reflected a secondary requirement with the goal of 
##expanding the project.In this script, the processing is exactly the same,
##but runs based on a user supplied subset of data rather than directly 
##on the CassavaBase server.

##Kyndra Zacherl, Boyce Thompson Institute at Cornell University, Summer 2018

##Installs and explanations for necessary packages!
##install.packages("pedigreemm")
##Used to properly format the pedigree for an additive matrix.
##install.packages("proxy")
##Used for calculating Euclidean marker distances
##install.packages("ggplot2")
##Visualization
##install.packages("foreach")
##Ability to do foreach loops similar to Java and C#

##install.packages("doMC")
##Used for running the analysis on multiple cores (i.e. faster). Not included here.

library(pedigreemm)
library(proxy)
library(dplyr)
library(foreach)
##library(doMC)

##Used to run the analysis in parallel, since the goal is largescale analysis
##and not just a subset of 120 accessions. The number of cores is detected and 
##a cluster is made of these cores - 1 (as to not overwork the machine).The
##doMC package only runs on select versions of R so I'm not including it here.
##cores <- (detectCores() -1)
##cl <- makeCluster(cores)
##registerDoMC(3)
##getDoParWorkers()

##For demonstration purposes, I've replaced the monstrous data sets with a subset
##featuring 2 pedigrees retrieved from CassavaBase (one found to be questionable
##and one found to be consistent in large scale analysis)and a positive  control
##(child, mother, and father are identical). Change the paths and try it out!
##Genotypes = 14genotypes-p4.txt and Pedigree = ThreePed.txt
genotype_data <- read.table ('C://Users//kzacherl//Downloads//14genotypes-p4.txt', header = TRUE, check.names = FALSE, stringsAsFactors = FALSE, na.strings = "na")
pedigree_data <- read.table ('C://Users//kzacherl//Documents//Git//Major_Projects//CassavaPedigree//RawData//ThreePed.txt', header = FALSE, sep = "\t", check.names = FALSE, stringsAsFactors = FALSE)

colnames(pedigree_data)[1] <- "Name"
colnames(pedigree_data)[2] <- "Mother"
colnames(pedigree_data)[3] <- "Father"

##When downloaded from CassavaBase, the accessions contain a pipe (|) after their
##names to designate individual plants. This removes them. Command line grep can
##also be used to edit the original file.
colnames(genotype_data) <- gsub('\\|[^|]+$', '', colnames(genotype_data))
pedigree_data["Pedigree Conflict"] <- NA
pedigree_data["Markers Skipped"] <- NA
pedigree_data["Informative Markers"] <- NA

length_p <- length(pedigree_data[,1])
potential_conflicts <-0
bad_data <-0
length_g <- length(genotype_data[,1])

##When downloading directly from CassavaBase, a problem arises where an extra row
##is automatically added with comments regarding the genotyping protocol so the
##first accession becomes the header row in R. When the file is over 1GB, it is 
##much easier to unlist the first row rather than open the original file to edit
##it. However, I've gone ahead and edited it since the file is only a small subset.
##rownames(genotype_data) <- as.character(unlist(genotype_data[,1]))
##genotype_data = genotype_data[,-1]

##This is the body of the analysis: running a foreach loop in parallel through
##all 120 children in the pedigree, comparing each child SNP dosage score with 
##the dosage scores of the mother and father in order to determine if the 
##combination is impossible. For example, if the mother and father each have 0
##of a certain allele, and a child has 2, this is ruled as impossible based on
##expected inheritence patterns.

##The total number of these "implausibilities" is tallied and divided by the 
##overall number of SNPs used in the analysis to produce a "pedigree conflict 
##score". If the score is over a certain threshold, these child accessions can be
##marked as likely having an incorrect parent recorded on their pedigree. I 
##placed my threshold at 2% to account for natural genetic mutations; 
##interestingly enough, even without a filter, a natural bimodal distribution
##forms within the data, with the valley located around 2% **.2% when doing small
##scale analysis**. 

results <- foreach (z = 1:length_p, .combine = rbind) %do%
  ##%dopar% if parallel computing is desired
{
  implausibility_count <- 0
  bad_data <- 0
  row_vector <- as.vector(pedigree_data[z,])

  test_child_name <- pedigree_data[z,1]
  test_mother_name <- pedigree_data[z,2]
  test_father_name <- pedigree_data[z,3]

  cat("Analyzing pedigree number", z, "...\n")
  
  for (q in 1:length_g)
  {
    child_score <- genotype_data[q, test_child_name]
    ##Rounding is an option here if it is not desired below
    ##child_score <- round (child_score, digits = 0)
    
    mother_score <- genotype_data[q, test_mother_name]
    ##mother_score <- round(mother_score, digits = 0)
    
    father_score <- genotype_data[q, test_father_name]
    ##father_score <- round(father_score, digits = 0)
    
    parent_score <- mother_score + father_score
    SNP <- as.vector(genotype_data[q,1])
    
    ##Skips SNPs where any pedigree member has an NA on file. 
    if ((is.na(child_score)) || (is.na(mother_score)) || (is.na(father_score))){
      bad_data <- bad_data + 1
      next  
    }
    
    ##Here, heterozygotes are removed. Heterozygotes are the lowest confidence 
    ##genetic read, since 1 allele cannot make as much of an inheritence 
    ##determination as a 0 or 2.
    if ((child_score == 1) || (mother_score == 1) || (father_score == 1)){
      bad_data <- bad_data + 1
      next  
    }
    
    ##Here, any dosage score that is not a whole number is removed. Due to the
    ##genotyping protocol being low-depth, some reads can be incomplete and 
    ##give results such as .5 or 1.7. These are similarily low confidence like
    ##heterozygotes, and are filtered out. This can also be remedied with the
    ##rounding function mentioned above, but it is used  here as it makes 
    ##more sense from a code logic perspective.
    if ((child_score != 0 && child_score != 2) || (mother_score != 0 && mother_score != 2) ||
    (father_score != 0 && father_score != 2)){
      bad_data <- bad_data +1
      next
    }
    
    ##Logical inheritence comparisons are made here. For example, if a mother
    ##has 2 alleles and a father has 2 alleles, it is impossible for the child
    ##to not have two (barring a drastic genetic mutation) and is marked here as
    ##"implausible" each time a genetic conflict occurs.
    if (child_score > parent_score) {
      implausibility_count <- implausibility_count + 1
    }
    else if ((mother_score == 2 && father_score == 2) && child_score != 2){
      implausibility_count <- implausibility_count + 1
    }
    else if ((mother_score == 2 || father_score == 2) && child_score == 0){
      implausibility_count <- implausibility_count + 1
    }
    ##"xor" represents an exclusive or (i.e. true if one and only one of the following 
    ##conditions are true)
    else if ((xor(mother_score == 2, father_score == 2)) && (xor(mother_score == 0, 
    father_score == 0)) && child_score == 2){
      implausibility_count <- implausibility_count + 1
    }
  }
  ##A conflict score is calculated by dividing the "implausible" tally by the 
  ##total number of SNPs contained in the selected genotyping protocol.
  
  ##A conflict score of .98 would represent that 98% of SNPs from the child
  ##could logically have been gained from its parents. Any score below .98 was 
  ##marked as a likely error, as determined from the bimodal distribution that 
  ##was previously mentioned.
  ##**As I was preparing the sample data set, I noticed a change in
  ##the way the data had been analyzed as a small set 
  ##that scaled the cutoff down to .002 rather than .02**!
  ##conflict_score <- 1 - (implausibility_count / length_g)
  
  ##This provides the conflict score the fraction of SNPs that were in conflict.
  ##A score of .98 would output .02 here, to represent 2% conflict.
  conflict_score <- implausibility_count / length_g

  ##This option is to output the scores as percents instead of decimals.
  #conflict_score<- sprintf("%.1f%%", dosage_score * 100)
  
  ##This option will output the overall number of SNPs that were found to be 
  ##valuable in the analysis.
  informative <- length_g - bad_data
  
  pedigree_data [z, 4] <- conflict_score
  pedigree_data [z, 5] <- bad_data
  pedigree_data [z, 6] <- informative
  
  return_vector <- pedigree_data [z,] 
  print(return_vector)
  flush.console()
}


hist(pedigree_data$'Pedigree Conflict', main = "Distribution of Pedigree Conflict Scores", breaks = 20, 
     xlab = "Pedigree  Conflict Scores", col = '#663300', labels = TRUE)

##A histogram that I made on the complete data set was generated using this code
##and is labeled "PortfolioHistogram.png" in my .zip folder. Check out that 
##bimodality!

#############################
#Visualization and Analysis#
############################

##This file contains the complete pedigrees of the 120 subset.
ped <- read.table ('C://Users/kzacherl//Documents//Git//Major_Projects//CassavaPedigree//RawData//120Ped.txt', header = FALSE, sep = "\t", check.names = FALSE, stringsAsFactors = FALSE)
colnames(ped)[1] <- "Name"
colnames(ped)[2] <- "Mother"
colnames(ped)[3] <- "Father"

##The PedigreeMM package is used here to format the pedigree file for 
##creation of an additive matrix.
P2= editPed(dam=ped$Mother, sire=ped$Father, label=ped$Name)
P3= pedigree(P2$sire, P2$dam, P2$label)
Amat <- getA(P3)

##This file will not be included since it is nearly 1G and took 48+ hours
##to calculate these marker distances.
genotype_data <- read.table ('C://Users//kzacherl//Downloads//genotypes.txt', header = TRUE, check.names = FALSE, stringsAsFactors = FALSE, na.strings = "na")
marker_distances <- dist(genotype_data, method = "Euclidean", by_rows = FALSE)

include_list <- labels(Amat[,"NR110112"])
subset_matrix <- dist_matrix[rownames(dist_matrix) %in% include_list, colnames(dist_matrix) %in% include_list] 

##Additive matrix manipulation code provided by 
##Bryan Ellerbrock, Cornell University Research Specialist 
x <- Amat[!rownames(Amat) %in% "NR110112","NR110112"]
y <- subset_matrix["NR110112",!colnames(subset_matrix) %in% "NR110112"]

xord <- order(names(x))
x <- x[xord]
yord <- order(names(y))
y <- y[yord]

ggplot()+ggtitle("NR110112 Relationships")+labs(x="Additive relationship",y="Marker-based distance")+geom_text(aes(x=x,y=y,label=names(x)))

##Ouput file is entitled "nr110112.txt". This plot compares the expected 
##relationships based on the additive matrix (a score of 0 is no relation, where
##a score of .05 would be parent) and compares them to the Euclidean marker 
##distances, used to calculate how far apart genetically certain accessions are.
##If an accession has a high pedigree conflict score and is found to possibly
##have an incorrect parent, this test is run to see if any other accessions that
##should have no relation (AM score of 0) have a small marker based distance
##(indicates close relation). Here, TMS-IBA980505 was seen to be extremely close
##genetically, with no known relation. This is the expected parent for this
##accession, and the next step is to contact the breeder to check if this is 
##actually possible and if it is, to update all records.

##This analysis was run to compare the scores generated with the user provided
##genotype data (using the R script) versus the Perl script analysis which
##directly accesses the database. The reason for the comparison was that 
##the database has the capability to automatically filter out SNPs with many
##NA values or a large quanitity of low confidence reads (1's and uneven numbers)
##Unfortunately, I am not able to directly access the database servers any longer
##and do not have access to the output data, but still have the code and the
##resulting graph.
newscores <- read.table("/home/klz26/sgn/bin/scoreswithstrictestfilter.txt", header = FALSE, check.names = FALSE, stringsAsFactors = FALSE)
originalscores <- read.table("/home/klz26/originalscores.txt", header= TRUE, check.names = FALSE, stringsAsFactors = FALSE, fill = TRUE)

colnames(newscores) [1] <- "Child"
colnames(newscores) [2] <- "Child ID"
colnames(newscores) [3] <- "Mother"
colnames(newscores)[4] <- "Mother ID"
colnames(newscores) [5] <- "Father"
colnames(newscores)[6] <-"Father ID"
colnames(newscores) [7] <- "Pedigree Conflict Score"
newscores[,4] <- (1 - newscores[,4])
newscores <- newscores[ , c("Accession", "Mother", "Father", "Conflict Score")]
newscores <- newscores[-c(1:5), ]

originalscores <- originalscores[order(originalscores[,'Accession']), ]
newscores <- newscores[order(newscores[,'Accession']),]

x= originalscores [,4]
y= newscores [,4]
labels =originalscores [,1] 
ggplot()+ggtitle("Comparison of R vs Perl Script")+labs(x="R Scores", y="Perl Scores")+ geom_text(aes(x=x,y=y,label =labels), size = 2.5)

##Resulting graph is saved as rvsperl.png, and shows little variation between the
##two languages, despite the automatic filtering done with the direct database
##connection.

##Also attached is my poster presentation for this project ("ZacherlBTIPoster.ppt").
##The beautiful stacked line graph that compares database filters was contributed
##by Dr. Guillaume Bauchet, but all other visualization was done by me personally.
##The pedigreevisualization was done by using a niche program known as Helium,
##and I've included that representation of all 120 accessions as 
##"helium_main_visualization.png"!
##~Fin