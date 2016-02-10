(ns confman.db
  (:require [com.stuartsierra.component :as component]))


(defrecord DB []
  component/Lifecycle
  (start [this]
    (println "associated kvs")
    (assoc this :kvs [1 2 3]))
  (stop [this]
    (dissoc this :kvs)))


(defn new-database []
  (DB.))


(defn get-kvs [db]
  (:kvs db))
