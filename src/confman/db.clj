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


;; returns a filter fn for either a exact macthing key,
;; or a prefix based match
(defn get-filter-fn [recurse]
  (if (= recurse "true")
    ;; prefix match
    (fn [kv prefix] (str/starts-with? (:key kv) prefix))
    ;; exact match
    (fn [kv key] (= (:key kv) key))))


(defn get-kvs [db prefix recurse]
  (let [kvs (:kvs db)]
    (filter #((get-filter-fn recurse) % prefix) kvs)
    ))
