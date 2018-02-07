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
