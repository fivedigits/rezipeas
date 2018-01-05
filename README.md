# rezipeas

This is a simple Web App written in Clojure using Compojure and HugSQL. There
is no authentication/authorization mechanism in place. Hence, it is advisable
to run this on your local network behind a firewall, without exposing it to the
internet.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

This Program is licensed under GPLv3. See LICENSE.
