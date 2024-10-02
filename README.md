# Usage

## Installation Instructions

-- **Download the Project Files:** 
   Start by downloading the files for this project from the repository.

## Instructions for Running the Code

To Run SBin-Shuffle, SAGeo-Shuffle, or S1Geo-Shuffle, execute `TestMain.java`. The `runTest` method of `TestMain` receives the epsilon value, delta value, dataset name, and the class of the corresponding data collector. 
Specify `SBinDataCollector.class`, `SAGeoDataCollector.class`, or `S1GeoDataCollector.class` to execute SBin-Shuffle, SAGeo-Shuffle, or S1Geo-Shuffle, respectively, for the class of the corresponding data collector.

### Configuration Settings:
- **Epsilon Value:** Set the desired value for epsilon in the `epsilons` variable.
- **Delta Value:** Set the desired value for delta in the `deltas` variable.
- **Type of encryption:** Set the encryption mode from {ENCRYPTION_MODE.Unencrypted, ENCRYPTION_MODE.ECIES, ENCRYPTION_MODE.RSA}. 

### Using Custom Datasets:
By default, the repository includes sample datasets used in our paper. If you wish to use your own dataset, such as "census.txt", simply set the `dataNames` variable to "census". Set your own dataset in `dataset` folder. 

### Sample execution:
- Compiling: javac -d ./bin -classpath ./lib/commons-math3-3.6.1.jar;./lib/bcprov-jdk15on-1.70.jar;./src src/lnf/TestMain.java
- Executing: java -classpath ./bin;./lib/commons-math3-3.6.1.jar;./lib/bcprov-jdk15on-1.70.jar lnf.TestMain

# Execution Environment
We used Rocky Linux 8.6 and Java 1.8.0.362, and Windows 11 Pro and Java 18.

# External Libraries used in our source code.
- [Apache Commons Math](https://commons.apache.org/proper/commons-math/) is distributed under the [Apache License 2.0].
- [Bouncy Castle](https://www.bouncycastle.org/) is distributed under the [MIT License].