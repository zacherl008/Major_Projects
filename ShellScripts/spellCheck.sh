# A Shell spell check program that is based on the package aspell. The user enters a word, and it is tested to make sure it isn't null.
#Next, the word is fed into aspell, and the result is egrepped for an asterick since aspell returns an asterick when a word is in its 
#dictionary. The user is informed if the word is spelled correctly or misspelled and then gives the option to check a new word.


#Kyndra Zacherl Spell Check
#added to /usr/bin
echo "Please make sure aspell is installed before using this script."

read -p "Enter the word you would like to spell check: " word

if [ -z $word ]
then
    #ending the script if the word provided is null
    echo "No word entered."
    exit
fi

#aspell returns an asterick if spelled correctly, this checks the word and if an asterick is returned
#-q flag doesn't print asterick
if aspell -a <<< "$word" | egrep -q '[*]';
  then
    echo "$word is spelled correctly."
  else
    echo "$word is misspelled."
fi

read -p "Would you like to check another word? Enter yes or no. " answer
if [ "$answer" == "yes" ];
  then
    spellCheck.sh
  else
    exit
fi
