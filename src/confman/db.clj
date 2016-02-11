(ns confman.db
  (:require [com.stuartsierra.component :as component]
            [clojure.string :as str]
            [taoensso.carmine :as car :refer (wcar)]))


(def conn {:pool {} :spec {}})
(defmacro wcar* [& body] `(car/wcar conn ~@body))

(defrecord DB []
  component/Lifecycle
  (start [this]
    (println "Connected to DB.")
    (assoc this :conn {:pool {} :spec {}}))
  (stop [this]
    (dissoc this :conn)))


(defn new-database []
  (DB.))


;; returns a filter fn for either a exact macthing key,
;; or a prefix based match
(defn get-filter-fn [recurse]
  (if (= recurse "true")
    ;; prefix match
    (fn [kv prefix] (str/starts-with? (:key kv) prefix))
    ;; exact match
    (fn [kv key] (= (:key kv) key))))


(defn get-all-kvs [db]
  (map (fn [[k v]] {:key k :value v})
   (car/wcar (:conn db) (car/parse-map (car/hgetall "kvs")))
   ))


(defn get-kvs [db prefix recurse]
  (let [kvs (get-all-kvs db)]
    (filter #((get-filter-fn recurse) % prefix) kvs)
    ))
