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
