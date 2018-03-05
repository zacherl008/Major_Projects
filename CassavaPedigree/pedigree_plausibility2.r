##This script is designed for largescale analysis of cassava pedigrees and 
##genotypes retrieved using the SGN cassava breeder database maintained by 
##the Lukas Mueller Lab at Boyce Thompson Institute at Cornell University 
##(https://www.cassavabase.org/).

##The accessions used in this analysis were selected using the CassavaBase
##Wizard with the specifications that all were genotyped using the latest 
##and most detailed genotyping protocol (GBS ApeKI V6) and all have parent lines 
##on record with their genotypes also stored in the database. In all,
##genotypes of 122 accessions and their parents were retrieved for analysis,
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
##
##install.packages("proxy")
##
##install.packages("dplyr")
##
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
##and not just a subset of 122 accessions. The number of cores is detected and 
##a cluster is made of these cores - 1 (as to not overwork the machine).The
##doMC package only runs on select versions of R so I'm not including it here.
##cores <- (detectCores() -1)
##cl <- makeCluster(cores)
##registerDoMC(3)
##getDoParWorkers()

##For demonstration purposes, I've replaced the monstrous data sets with a subset
##featuring 3 pedigrees retrieved from CassavaBase, a negative control
##(the father is a non-cassava plant), and positive  control (child, mother, 
##and father are identical).
genotype_data <- read.table ("C://Users//kzacherl//Downloads//18genotypes-p4.txt", header = TRUE, check.names = FALSE, stringsAsFactors = FALSE, na.strings = "na")
pedigree_data <- read.table ('C://Users//kzacherl//Documents//Git//Major_Projects//CassavaPedigree//RawData//negcontrol.txt', header = FALSE, sep = "\t", check.names = FALSE, stringsAsFactors = FALSE)

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
##z and q are set to one for looping.
z <- 1
q <- 1
geno.bad <- 0
exclude_list <- 0

##When downloading directly from CassavaBase, a problem arises where an extra row
##is automatically added with comments regarding the genotyping protocol so the
##first accession becomes the header row in R. When the file is over 1GB, it is 
##much easier to unlist the first row rather than open the original file to edit
##it. However, I've gone ahead and edited it since the file is only a small subset.
##rownames(genotype_data) <- as.character(unlist(genotype_data[,1]))
##genotype_data = genotype_data[,-1]

##Filtering function provided by Guillaume Bauchet, Cornell Post Doctoral Scientist.
##The user provides 3 valuesfor the filter, detailed below.
##"geno": data set of genotypes
##"IM": individual missing
##"MM": markers missing
##"H": heterozygotes

filter.fun <- function(geno,IM,MM,H){
  #Remove individuals with more than a % missing data
  individual.missing <- apply(geno,1,function(x){
    return(length(which(is.na(x)))/ncol(geno))
  })
  #length(which(individual.missing>0.40)) #will tell you how many
  #individulas needs to be removed with 20% missing.
  #Remove markers with % missing data
  marker.missing <- apply(geno,2,function(x)
  {return(length(which(is.na(x)))/nrow(geno))
    
  })
  length(which(marker.missing>0.6))
  #Remove markers herteozygous calls more than %.
  heteroz <- apply(geno,1,function(x){
    return(length(which(x==0))/length(!is.na(x)))
  })
  
  filter1 <- geno[which(individual.missing<IM),which(marker.missing<MM)]
  #filter2 <- filter1[,(heteroz<H)]
  return(filter1)
}
filter.fun <- function(geno,IM,MM){
  #Remove individuals with more than a % missing data
  individual.missing <- apply(geno,1,function(x){
    return(length(which(is.na(x)))/ncol(geno))
  })
  #length(which(individual.missing>0.40))
  #will tell you how many individuals needs to be removed with 20% missing.
  
  #Remove markers with X% missing data
  marker.missing <- apply(geno,2,function(x)
  {return(length(which(is.na(x)))/nrow(geno))
  })
  length(which(marker.missing>0.6))
  #Remove markers herteozygous calls more than %
  
  filter <- geno[which(individual.missing<IM),which(marker.missing<MM)]
  return(filter)
}

##Runs the filter on all of the SNPs contained in the genotype data (359,791 SNPs
##,row 1 is a header row). These SNPs are then added to an exclude list, and are  
##removed from each accession row by creating a subset.

##The options below (remove individuals and SNPs with more than 10% missing
##data) remove 107,
geno.bad <- filter.fun(genotype_data[2:359792,],0.5,0.5)
exclude_list <- geno.bad
subset_matrix <- genotype_data[!(rownames(genotype_data) %in% rownames(exclude_list)),] 

##This is the heart of the analysis.Running a foreach loop in parallel through
##all 122 children in the pedigree comparing each child SNP dosage score with 
##the dosage scores of the mother and father in order to determine if the 
##combination is impossible. For example, if the mother and father each have 0
##of a certain allele, and a child has 2, this is ruled as impossible based on
##expected inheritence patterns.

##The total number of these "impossibilities" is tallied and divided by the 
##overall number of SNPs used in the analysis to produce a "pedigree conflict 
##score". If the score is over a certain threshold, these child accessions can be
##marked as likely having an incorrect parent recorded on their pedigree. I 
##placed my threshold at 2% to account for natural genetic mutations; 
##interestingly enough, even without a filter, a natural bimodal distribution
##forms within the data, with the valley located around 2%. The intensity of this 
##bimodality increases with stricter filtering, as more low quality SNPs are removed.

results <- foreach (z = 1:5, .combine = rbind) ##%dopar% Used with the doMC package.
{
  implausibility_count <- 0
  bad_data <- 0
  row_vector <- as.vector(pedigree_data[z,])

  test_child_name <- pedigree_data[z,1]
  test_mother_name <- pedigree_data[z,2]
  test_father_name <- pedigree_data[z,3]

  cat("Analyzing pedigree number", z, "...\n")
  
  #if (test_father_name == "NULL" || test_child_name == "NULL" || test_mother_name == "NULL"){
  #  print ("Genotype information not all present, skipping analysis")
  #  break
  #}
  
  for (q in 1:length_g)
  {
    child_score <- subset_matrix[q, test_child_name]
    ##Option of rounding the 3 scores to a desired number of digits is provided 
    #child_score <- round (child_score, digits = 0)
    
    mother_score <- subset_matrix[q, test_mother_name]
    #mother_score <- round(mother_score, digits = 0)
    
    father_score <- subset_matrix[q, test_father_name]
    #father_score <- round(father_score, digits = 0)
    
    parent_score <- mother_score + father_score
    SNP <- as.vector(subset_matrix[q,1])
    
    if ((is.na(child_score)) || (is.na(mother_score)) || (is.na(father_score))){
      bad_data <- bad_data + 1
      next  
    }
    if ((child_score == 1) || (mother_score == 1) || (father_score == 1)){
      bad_data <- bad_data + 1
      next  
    }
    if ((child_score != 0 && child_score != 2) || (mother_score != 0 && mother_score != 2) ||
    (father_score != 0 && father_score != 2)){
      bad_data <- bad_data +1
      next
    }
    
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
  conflict_score <- implausibility_count / length_g
  #conflict_score<- sprintf("%.1f%%", dosage_score * 100)
  #pedigree_data [z, 4] <- conflict_score
  #pedigree_data [z, 5] <- bad_data
  informative <- length_g - bad_data
  #pedigree_data [z,6] <- informative
  #cat(pedigree_data$Name,pedigree_data$Mother,pedigree_data$Father,pedigree_data$`Pedigree Conflict`, pedigree_data$`Markers Skipped`,
  #    pedigree_data$`Informative Markers`,file=f_out,sep=" ",append=TRUE);
  return_vector <- pedigree_data [z,] 
  return_vector [6] <- informative
  return_vector [5] <- bad_data
  return_vector[4] <- conflict_score
  return_vector
}



pedigree_data$`Percent Removed` <- (pedigree_data$`Markers Skipped` / length_g ) * 100

hist(pedigree_data$'Pedigree Conflict', main = "Distribution of Pedigree Conflict Scores", breaks = 20, 
     xlab = "Pedigree  Conflict Scores", col = '#663300', labels = TRUE)

pedigreedata2 <- editPed(dam=pedigree_data$Mother, sire=pedigree_data$Father, label=pedigree_data$Name)
#pedigreeNoNAS <- pedigreedata2 [,complete.cases(pedigreedata2) ]
pedigreedata3 <- pedigree(pedigreedata2$sire, pedigreedata2$dam, pedigreedata2$label)
cassavaAmat <- getA(pedigreedata3)
# example: cassavaAmat[,"NR110122"]

#46 (38%), 76 (62%)
#anomFilterBAF selects segments which are likely to be anomalous.

#pedigree_data$`Pedigree Conflict` <- sprintf("%.1f%%", pedigree_data$`Pedigree Conflict` * 100)
#install.packages(GWAStools)

#column_vector <- as.vector(pedigree_data[x,])


