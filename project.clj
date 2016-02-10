(defproject confman "0.1.0-SNAPSHOT"
  :description "HTTP interface for a key-value store."
  :url "http://github.com/cskksc/confman"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.1.19"]
                 [com.stuartsierra/component "0.3.1"]
                 [compojure "1.4.0"]]
  :main confman.system
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[reloaded.repl "0.2.1"]] }
             })
