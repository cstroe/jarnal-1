#!/bin/bash

mkdir jarnal;
mkdir jarnal/DEBIAN;
mkdir jarnal/usr;
mkdir jarnal/usr/bin;
mkdir jarnal/usr/share;
mkdir jarnal/usr/share/applications;
mkdir jarnal/usr/share/jarnal;
mkdir jarnal/usr/share/mime;
mkdir jarnal/usr/share/mime/packages;
mkdir jarnal/usr/share/pixmaps;


echo 'Package: jarnal
Version: 1.0
Section: admin
Priority: optional
Architecture: all
Essential: no
Depends: openjdk-6-jre | sun-java5-jre | sun-java6-jre
Installed-Size: 11700
Maintainer: Gerhard Hagerer <ghagerer@googlemail.com>
Description: Jarnal - A programm for notetaking and sketching with a stylus.
 Jarnal is a plattform-indepenent programm for notetaking, sketching and
 PDF-/picture-annotating. The way of using it remembers of a normal notepad
 with the difference of being digital. It is thought to be used with a stylus
 on tablets or tablet-pcs. 
 
 It is written in Java and therefore needs a Java
 runtime environment (>=1.4.2).' >> jarnal/DEBIAN/control;

echo '#!/bin/sh
update-mime-database /usr/share/mime/' >> jarnal/DEBIAN/postinst;
chmod 755 jarnal/DEBIAN/postinst;

echo '#!/bin/sh
update-mime-database /usr/share/mime/' >> jarnal/DEBIAN/postrm;
chmod 755 jarnal/DEBIAN//postrm;

echo '#!/bin/bash
cd /usr/share/jarnal
./jarnal.sh "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8"' >> jarnal/usr/bin/jarnal;
chmod 755 jarnal/usr/bin/jarnal;

echo '#!/bin/bash
cd /usr/share/jarnal
./jarnalannotate.sh "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8"' >> jarnal/usr/bin/jarnalannotate;
chmod 755 jarnal/usr/bin/jarnalannotate;

echo '[Desktop Entry]
Version=1.0
Encoding=UTF-8
Name=Jarnal
Comment=A programm for notetaking an sketching with a stylus.
Exec=jarnal
Terminal=false
Type=Application
Categories=GNOME;GTK;Graphics;
MimeType=application/jarnal-jaj;' >> jarnal/usr/share/applications/jarnal.desktop;

echo '[Desktop Entry]
Version=1.0
Encoding=UTF-8
Name=Jarnal (annotate)
Comment=A programm for notetaking an sketching with a stylus.
Exec=jarnalannotate
Terminal=false
Type=Application
Categories=GNOME;GTK;
MimeType=application/pdf;image/bmp;image/gif;image/tiff;image/jpeg;image/png;image/svg+xml;application/jarnal-background;' >> jarnal/usr/share/applications/jarnalannotate.desktop;

echo '<?xml version="1.0" encoding="UTF-8"?>
<mime-info xmlns="http://www.freedesktop.org/standards/shared-mime-info">
   <mime-type type="application/jarnal-jaj">
     <comment>jarnal file</comment>
     <glob pattern="*.jaj"/>
   </mime-type>
   <mime-type type="application/jarnal-background">
     <comment>background file</comment>
     <glob pattern="*.jbg"/>
   </mime-type>
   <mime-type type="application/jarnal-meta">
     <comment>metadatafile</comment>
     <glob pattern="*.jmf"/>
   </mime-type>
</mime-info>' >> jarnal/usr/share/mime/packages/jarnal.xml;

wget http://www.dklevine.com/general/software/tc1000/jarnal-install.zip;
unzip jarnal-install.zip -d jarnal/usr/share/jarnal;
chmod 755 jarnal/usr/share/jarnal/jarnal.sh
chmod 755 jarnal/usr/share/jarnal/jarnalannotate.sh
cp jarnal/usr/share/jarnal/lib/jarnal.png jarnal/usr/share/pixmaps;
rm jarnal-install.zip
dpkg -b jarnal jarnal-1.0-all.deb;
gdebi-gtk jarnal-1.0-all.deb;
