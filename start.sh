#!/bin/bash
echo "======================================================="
echo "compila aplicacion"
mvn clean package -Dmaven.test.skip=true
echo "======================================================="
echo "======================================================="
echo "compila imagen docker - dk_fshospedaje"
docker build -t dk_fshospedaje .
echo "======================================================="
echo "======================================================="
echo "corre imagen del contenedor"
docker run --name dk_fshospedaje \
-d -p 3021:21 -p 3020:20 -p 13020:12020 -p 13021:12021 -p 13022:12022 -p 13023:12023 -p 13024:12024 -p 13025:12025  \
-e "USER=touresbalon" -e "PASS=verysecretpwd" \
-v /data/ftp/fshospedaje:/ftp \
-it dk_fshospedaje
echo "======================================================="
echo "======================================================="
echo "fin"
