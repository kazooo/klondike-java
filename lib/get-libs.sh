 #!/bin/bash

FILE=images.zip
URL=https://raw.githubusercontent.com/xermak00/klondike-java/master/${FILE}
DIR=`pwd`

if [ "`basename ${DIR}`" != "lib" ]
then 
	DIR="./lib"
else
	DIR="."
fi

wget --directory-prefix=${DIR} ${URL}
unzip ${DIR}/${FILE} -d ${DIR}
