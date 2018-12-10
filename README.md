# BeSEPPI
BeSEPPI is a benchmark for semantic based benchmarking of property path implementations.

In order to use BeSEPPI you can either clone the java classes and run it as java application, or you can use the executable jars in the executable subdirectory.

## Use Executables
In the executables subdirectory there are two jars.
* BenchmarkSuite.jar
* BeSEPPI.jar

Executing BenchmarkSuite.jar will provide a gui that allows to chose between the benchmark suite or the result viewer. With the help of the result viewer csvs hold resuls of benchmarks can be displayed. With the benchmark suite benchmarks can be executed on RDF stores.
To add a benchmark or store click the respective buttons and fill in the information. After that the benchmark can be executed and the result will be displayed with the result viewer.

If there is no need for a gui and the benchmark should be executed from the command line, use BeSEPPI.jar. BeSEPPI.jar takes 5 arguments:
-path to Benchmark -path to result -store query endpoint -store update endpoint -store data endpoint 
For instance, to execute BeSEPPI on Apache Jena Fuseki use (all in one row):
```
java -jar BeSEPPI.jar
/path/to/git/BeSEPPI/Benchmark 
/path/to/result/ 
http://localhost:3030/ds/query 
http://localhost:3030/ds/update 
http://localhost:3030/ds/data
```
Note that you can use any dataset, queries and reference result set for benchmarking with the benchmark suite. The directory where the benchmark is has to include a queries and a dataset subdirectory. The dataset subdirectory can hold RDF files, which will be loaded to the store. The queries subdirectory should hold queries with the ending .sparql and reference result sets with the ending .csv.
For instance: 
query1.sparql query1.csv
query2.sparql query2.csv 
[...]

## Results
The results subdirectory holds information of some stores that were already benchmarked with BeSEPPI.
