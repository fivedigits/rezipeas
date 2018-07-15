(defproject rezipeas "0.8.3"
  :description "A simple web app recipe server"
  :url "http://github.com/fivedigits/rezipeas"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [enlive "1.1.6"]
                 [com.layerware/hugsql "0.4.8"]
                 [org.xerial/sqlite-jdbc "3.21.0.1"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler rezipeas.handler/app
         :nrepl {:start? true :port 9998}
         :port 3000}
  :profiles
  {:prod {:ring {:port 80
                 :stacktraces? false}}
   :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
