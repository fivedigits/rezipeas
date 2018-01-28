# rezipeas

This is a simple recipe server web app written in Clojure using Compojure and HugSQL. There
is no authentication/authorization mechanism in place. Hence, it is advisable
to run this on your local network behind a firewall, without exposing it to the
internet. A use case might be to run the program on your ARM-computer (e.g. RaspberryPi, BeagleBoard etc.).

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## Building a standalone executable

If you want to to create an executable .jar, you can run

   lein ring uberjar

If you want to run the application on port 80, use

   lein with-profiles +prod ring uberjar

Note that you will need root access to run the executable. The .jar is then located under

     target/rezipeas-X.Y.Z-standalone.jar

Run the file using

    java -jar rezipeas-X.Y.Z-standalone.jar

When running as root, the data will be saved under

     /var/www/rezipeas

## License

This Program is licensed under GPLv3. See LICENSE.
