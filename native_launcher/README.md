Launcher script for Juicebox CLT with native libraries.
Copy the JuiceboxCLT.jar to this folder, then call

bash juicebox.sh hiccups -c 1 -r 5000 -m 5000 <hic file url> <output1> <output2>

to run hiccups. Native libraries are currently only needed for hiccups, though this script can be used for all
command line tools.


    NOTE: by default, this code is built using JCuda 0.7, which is for CUDA 7.
    We have also included zipped files for JCuda 0.7.5 (for CUDA 7.5) under the
    libs/jcuda folder. When building the appropriate juicebox jar, specify the
    JCuda version in the build.xml file (2 locations) and ensure the the natives
    folder contains the native libraries for the appropriate version.

    More details and jars/native libraries for other versions of JCuda
    may be found at the JCuda website(http://www.jcuda.org/).
