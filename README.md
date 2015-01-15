## About

`morphBMC` is my Master's Thesis project. [My thesis website](https://wwwmatthes.in.tum.de/pages/2zg3vweodthz/Master-s-Thesis-Dominique-Busser) contains more specific information about the thesis and research results.

This application lets you solve Wicked Problems (Rittel, 1973) by leveraging the power of General Morphological Analysis (GMA) collaboratively. A specific use case is the generation and exploration of business models using the combination of GMA and the Business Model Canvas (Osterwalder/Pigneur, 2010).

###The application supports 6 steps to execute a GMA:

####1. Definition

Each participant breaks down the problem into parameters and attributes. Each parameter (e.g. "Cost") can take on one of its attributes (e.g. $100, $200+) as its value.

####2. Refinement

The collaborators agree on a common model. The results of this steps are synchronized in real time and the final result defines the problem model.

####3. Compatibility

Each participant defines a compatibility rating for every possible combination of attributes (e.g. "Cost" = $100 and "Distance" = 50mi => *good fit*). The compatibility describes how well the selected attributes match.

####4. Resolution

In order to resolve conflicts, where multiple collaborators rated compatibilities differently, this steps lets the collaborators finalize the compatibility ratings. Conflicting ratings are highlighted and can be overriden. This steps completes the problem model and concludes the collaborative phase.

####5. Exploration

Each participant can explore possible solutions by arbitrarily selecting attributes and exploring which remaining attributes have the best fit. This is done in a transitive matter, so all selected attributes are considered.

####6. Results

Participants can view all possible results for the problem. The consistency value for each solution describes how well its attribute combination match. A high consistency value represents an excellent solution.



## Running morphBMC

In order to run `morphBMC`, you need to install the [Play! Framework](https://www.playframework.com/). You should also set up a PostgreSQL database server and configure the `conf/application.conf` file for database access. Execute `activator run` in the project root directory to deploy the application to a local web server, or use `activator -jvm-debug 9999 ~run` to debug it.
