# DataMiningFromBiologicalDataGraphs

This is my thesis about data mining from biological data and finding similarities (patterns) between graphs (in the field of bioinformatics)

## Abstract

The extraction of useful knowledge through data mining is one of the most important challenges in many scientific communities, including Bioinformatics. When the data used represents a set of independent entities and their properties, data mining is crowned almost always with great success. Specifically, interesting knowledge can be mined from the relationships between entities, given that they are represented in an appropriate form. The representation through graphs is an efficient and effective modeling of the data as a collection of nodes and edges. It also supports all aspects of relational data mining process aiming to find common patterns/sub-graphs. In the field of Biology, graphs are being used for the representation of many different types of data: from graphs of interactions between proteins to metabolic pathways, and from the display of protein sequence to the modeling of phylogenetic trees. Despite the use of graphs, however, there is a clear lack of such data mining algorithms, particularly in the field of Bioinformatics.

Primary goal of this work is to develop an algorithm capable of identifying evolutionary patterns in the metabolic pathways of various organisms and to group the latter into groups that share common characteristics. Additionally, and in terms of implementation, the tool should be able to run in a reasonable time using a conventional terminal. An equally important requirement is user-friendliness in order to facilitate its use by as much as possible members of the research community.

The implemented tool manages to run in a reasonable time, while obfuscating from the user the computational complexity of finding the evolutionary patterns, by providing him an interface where he can easily process only the output of the tool. For validation purposes and in order to test and confirm the desired theoretical design, experiments were conducted using artificial data with basic graph topologies and with prior expected behavior. Finally, experiments were conducted with real-world data yielding interesting patterns of the selected metabolic pathways.