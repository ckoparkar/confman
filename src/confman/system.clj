(ns confman.system
  (:require [com.stuartsierra.component :as component]
            [confman.db :refer [new-database]]
            [confman.server :refer [new-server]]))

(defn system []
  (component/system-map
   :db (new-database)
   :server (new-server)))

(defn start-system []
  (component/start system))
