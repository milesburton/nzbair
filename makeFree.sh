#!/bin/bash

SCRATCH_DIRECTORY=/tmp/AirCollectionFree
BACKUP_FILE=/tmp/backup-nzbair-`date +%s`.tar.gz

# Setup environment
echo "Backing up... to $BACKUP_FILE"
tar cvzf $BACKUP_FILE . >> /dev/null

if [ ! -f $BACKUP_FILE ]
then
    echo "Backup does not exist. Exiting!" 
fi

if [ -d $SCRATCH_DIRECTORY ]
then
	echo "Scratch directory aleady exists. Deleting"
	rm -rf $SCRATCH_DIRECTORY
fi

echo "Creating scratch directory $SCRATCH_DIRECTORY"

mkdir $SCRATCH_DIRECTORY
echo "Copying files into scratch"
cp -r . $SCRATCH_DIRECTORY >> /dev/null

if [ ! -d $SCRATCH_DIRECTORY ]
then
    echo "Scratch directory does not exist Exiting!" 
fi

echo "Moving to scratch"
cd $SCRATCH_DIRECTORY
echo "Current directory: `pwd`"

echo "Cleaning up directories if needed"
rm -rf app/bin 
rm -rf app/gen

echo "Moving NZBAir Premium package to Free"
NZBAIR_PREMIUM_PACKAGE=app/src/main/java/com/mb/android/nzbAirPremium
NZBAIR_FREE_PACKAGE=app/src/main/java/com/mb/android/nzbAirFree

mv $NZBAIR_PREMIUM_PACKAGE $NZBAIR_FREE_PACKAGE

if [ $? -ne 0 ]
then
    echo "Failed to move NZBAIr package"
    exit 1
fi

echo "Replacing all references to NZBAirPremium"
find app/ -name '*.java' |xargs perl -pi -e 's/nzbAirPremium/nzbAirFree/g'

if [ $? -ne 0 ]
then
    echo "Failed to rename java packages"
    exit 1
fi


find app/ -name '*.xml' |xargs perl -pi -e 's/nzbAirPremium/nzbAirFree/g'

if [ $? -ne 0 ]
then
    echo "Failed to rename xml packages"
    exit 1
fi

find app/ -name '*.aidl' |xargs perl -pi -e 's/nzbAirPremium/nzbAirFree/g'

if [ $? -ne 0 ]
then
    echo "Failed to rename xml packages"
    exit 1
fi

echo "Removing all annotated references to premium content"

find app/ -name *.java -print | xargs sed -f remove_premium.sed -i
find app/ -name *.xml -print | xargs sed -f remove_premium.sed -i
find app/ -name *.aidl -print | xargs sed -f remove_premium.sed -i

