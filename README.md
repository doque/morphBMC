## About

`morphBMC` is my Master's Thesis project. [https://wwwmatthes.in.tum.de/pages/2zg3vweodthz/Master-s-Thesis-Dominique-Busser](The university's website) contains more specific information.

`morphBMC` lets you solve Wicked Problems (Rittel, 1973) by utilizing the power of General Morphological Analysis (GMA) in a collaborative matter. A specific use case is the generation and exploration of business models using the combination of GMA and the Business Model Canvas (Osterwalder/Pigneur, 2010).

In order to run `morphBMC`, you need to install the [https://www.playframework.com/](Play! Framework). You should also set up a PostgreSQL database server and configure the `conf/application.conf` file for database access. Execute `activator run` in the project root directory to deploy the application to a local web server, or use `activator -jvm-debug 9999 ~run` to debug it.
