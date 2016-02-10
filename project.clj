(defproject confman "0.1.0-SNAPSHOT"
  :description "HTTP interface for a key-value store."
  :url "http://github.com/cskksc/confman"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.1.19"]]
  :main confman.server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
