--------------
About Juicebox
--------------
Juicebox is visualization software for Hi-C data.  In this distribution, we
include both the visualization software itself and command line tools for
creating and analyzing files that can be loaded into Juicebox.


Check out the <a href="http://aidenlab.org/juicebox">Juicebox website</a>
for more details on how to use Juicebox, as well as following a detailed
tutorial.

Juicebox was created by
<a href="https://github.com/jrobinso">Jim Robinson</a>,
<a href="https://github.com/nchernia">Neva C. Durand</a>, and
<a href="http://www.erez.com/">Erez Lieberman Aiden</a>.

Ongoing development work is carried out by
<a href="https://github.com/nchernia">Neva C. Durand</a>,
<a href="https://github.com/bluejay9676">Jay Ryu</a>,
<a href="https://github.com/asddf123789">Fanny Huang</a>,
<a href="https://github.com/mikeehman">Nam Hee Kim</a>,
<a href="https://github.com/imachol">Ido Machol</a>,
<a href="https://github.com/nguyenkvi">Vi Nguyen</a>,
and <a href="https://github.com/sa501428">Muhammad Saad Shamim</a>.

Past contributors include
<a href="https://github.com/zgire">Zulkifl Gire</a> and
<a href="https://github.com/mhoeger">Marie Hoeger</a>.

--------------
IntelliJ Setup
--------------

Use IntelliJ IDEA (Community edition - free)

To set up in IDEA, have the Java SDK installed and link it with IntelliJ (IntelliJ has lots of
documentation on this and will bring it up as a popup if it's not done).

* Then go to `VCS` -> `checkout from version control`. (You'll need to have forked the Juicebox repo)
* You'll need to do is be sure `*.sizes` is included as a file to be copied over to the class files.
Set this up via IntelliJ `Preferences` -> `Compiler`. Add `?*.sizes` to the list of `Resource Patterns`.
* While there, also go to `Java Compiler` and put this into additional command line options: `-Xlint:all -target 1.7`
The former turns on all warnings, the latter gives some flexibility since some people haven't updated Java to 1.8 yet.
* Then go to `Run` -> `Edit Configurations`.
* With the `+` sign, add `Application`.
* You'll create two of these, one for the GUI (call it Juicebox GUI or whatever you want, really) and one for the CLT.
* Set the main class by clicking the little `...` button next to the text box for main class

        MainWindow.java is the main method class for the visualization/GUI portion of the software.
        HiCTools.java is the main method class for the analysis/CLT portion.

* For the GUI under VM Options:

        -Xmx2000m
        -Djnlp.loadMenu="http://hicfiles.tc4ga.com/juicebox.properties"

* For the CLT use

        -Xmx2000m

* Note that the `Xmx2000m` flag sets the maximum memory heap size to 2GB.
Depending on your computer you might want more or less.
Some tools will break if there's not enough memory and the file is too large,
but don't worry about that for development; 2GB should be fine.
* One last note: be sure to `Commit and Push` when you commit files, it's hidden in
the dropdown menu button in the commit window.

------------
Distribution
------------
The files included in this distribution are as follows:
* README
* build.xml and juicebox.properties (for jar compilation from source)
* src (directory containing source code)
* lib (directory containing libraries for compilation)
* data (directory containing test data)

Executables for Juicebox can be downloaded from http://aidenlab.org/juicebox
The latest command line tools jar can be downloaded from http://aidenlab.org/commandlinetools

-------------
Documentation
-------------
We have extensive documentation for how to use Juicebox at
<a href="http://aidenlab.org/juicebox/">aidenlab.org/juicebox/</a> including a video,
a Quick Start Guide, and a detailed tutorial.

Extensive documentation for using the command line tools is available at
<a href="http://aidenlab.org/commandlinetools/">aidenlab.org/commandlinetools/</a>.

----------------------------------
Hardware and Software Requirements
----------------------------------
The minimum software requirement to run Juicebox is a working Java installation
(version > 1.6) on Windows, Linux, and Mac OSX.  We recommend using the latest
Java version available, but please do not use the Java Beta Version. Minimum
system requirements for running Java can be found at
http://java.com/en/download/help/sysreq.xml. To download and install the latest
Java Runtime Environment (JRE), please go to http://www.java.com/download.

We recommend having at least 2GB free RAM for the best user experience with
Juicebox.

To launch the Juicebox application from command line, type
  java -Xms512m -Xmx2048m -jar Juicebox.jar

To launch the command line tools, run the shell script “juicebox” on Unix or
MacOS, run the batch script "juicebox.bat" on Windows, or type
  java -Xms512m -Xmx2048m -jar Juicebox_CLT.jar

Note: the -Xms512m flag sets the minimum memory heap size at 512 megabytes, and
the -Xmx2048m flag sets the maximum size at 2048 megabytes (2 gigabytes). These
values may be adjusted as appropriate for your machine.

--------------------------------
Compiling Jars from Source Files
--------------------------------
1. You should have Java 1.8 JDK and Apache Ant installed on your system. See
   below for more information.
2. Go to the folder containing the Juicebox source files and edit the
   juicebox.properties file with the proper Java JDK Address.
3. Open the command line, navigate to the folder containing the build.xml file
   and type
     ant
   The process should take no more than a minute to build on most machines.
4. The jars are written to the directory out/.  You can change this by editing
   the build.xml file.

* Installing Java 1.8 JDK

For Windows/Mac/Linux, the Java 1.8 JDK can be installed from here:

http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

(Alternative) For Ubuntu/LinuxMint

http://tecadmin.net/install-oracle-java-8-jdk-8-ubuntu-via-ppa/

* Installing Apache Ant
Mac
  Ant should be installed on most Macs. To verify installation via the command
  prompt, type
    `ant -version`
  If Ant is not on your Mac, install it via homebrew. At the command prompt, type
```    
brew update
brew install ant
```
  You may need to install Homebrew (http://brew.sh/) on your machine
  
  See the following Stackoverflow post for more details:

  http://stackoverflow.com/questions/3222804/how-can-i-install-apache-ant-on-mac-os-x

Windows
  Installing Ant requires some minor changes to your system environment. Follow
  the instructions in this article:

  http://www.nczonline.net/blog/2012/04/12/how-to-install-apache-ant-on-windows/

Linux
  In the command prompt, type
    `sudo apt-get install ant`
  or
    `sudo yum install ant`
  depending on your package installer
