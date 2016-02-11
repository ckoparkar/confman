(ns confman.db
  (:require [com.stuartsierra.component :as component]
            [clojure.string :as str]))


(defn stub-kvs []
  [{:key "helpshift/hello/world" :value "world"}
   {:key "helpshift/hello/you" :value "you"}
   {:key "com/google/in" :value "in"}])

(defrecord DB []
  component/Lifecycle
  (start [this]
    (println "associated kvs")
    (assoc this :kvs (stub-kvs)))
  (stop [this]
    (dissoc this :kvs)))


(defn new-database []
  (DB.))


(defn get-kvs [db prefix]
  (let [kvs (:kvs db)]
    (filter #(str/starts-with? (:key %)prefix) kvs)
    ))
