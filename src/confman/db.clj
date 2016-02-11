(ns confman.db
  (:require [com.stuartsierra.component :as component]
            [clojure.string :as str]
            [taoensso.carmine :as car :refer (wcar)]))


(def conn {:pool {} :spec {}})
(defmacro wcar* [& body] `(car/wcar conn ~@body))


(defn stub-acls []
  {"anonymous" ["helpshift"]
   "master" []})


(defrecord DB []
  component/Lifecycle
  (start [this]
    (println "Connected to DB.")
    (merge this {:acl (stub-acls), :conn {:pool {} :spec {}}}))
  (stop [this]
    (merge this (dissoc this :acl) (dissoc this :conn))))


(defn new-database []
  (DB.))


;; returns a filter fn for either a exact macthing key,
;; or a prefix based match
(defn- get-prefix-filter-fn [recurse]
  (if (= recurse "true")
    ;; prefix match
    (fn [kv prefix] (str/starts-with? (:key kv) prefix))
    ;; exact match
    (fn [kv key] (= (:key kv) key))))


(defn- get-acl-filter-fn [db token]
  (let [token (or (contains? (:acl db) token) :anonymous)
        rules (get-in db [:acl token])]
    (fn [kv]
      (not-any? #(str/starts-with? (:key kv) %) rules))))


(defn- get-all-kvs [db]
  (map (fn [[k v]] {:key k :value v})
   (car/wcar (:conn db) (car/parse-map (car/hgetall "kvs")))))


(defn get-kvs [db prefix recurse token]
  (let [kvs (get-all-kvs db)]
    (->> kvs
       (filter #((get-acl-filter-fn db token) %))
       (filter #((get-prefix-filter-fn recurse) % prefix)))))
