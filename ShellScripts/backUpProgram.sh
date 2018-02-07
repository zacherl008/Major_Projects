#This program is an original backup program designed to be used with Shell. It prompts the user for the username that they wish to backup
#and if no backup file is present, one is created. A  tar ball is then made of everything contained in the user account and added to the 
#archives folder.
#Kyndra Zacherl Backup Program
#! /bin/bash/
read -p "Enter a user name:" USER
echo "Backing up the files of $USER"

cd ..

if [ -d /home/${USER}/archives ]
then
        echo "An archives folder is present"
        cd /home/${USER}/archives
else
        echo "An archives folder is not present. Creating one now."
	mkdir /home/${USER}/archives
fi

tar -cvpzf /home/${USER}/archives/${USER}.tar.gz /home/${USER}

echo "File backup completed."
