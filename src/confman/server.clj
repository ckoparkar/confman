(ns confman.server
  (:require [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [com.stuartsierra.component :as component]
            [confman.db :as db]
            [clojure.data.json :as json]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]))


(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello world"})


(defn get-kvs [req]
  (let [prefix (get-in req [:params :*])
        recurse (get-in req [:params :recurse])
        kvs (db/get-kvs (::db req) prefix recurse)]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body (json/write-str kvs)
     }))


(defroutes routes
  (GET "/" [] index)
  (GET "/v1/kv/*" [] get-kvs)
  (route/not-found "<h1>Page not found</h1>"))


(defn wrap-db-component [f db]
  (fn [req]
    (f (assoc req ::db db))))


(defn make-handler [db]
  (-> routes
     (wrap-db-component db)
     wrap-keyword-params
     wrap-params))


(defn start-server [handler port]
  (println (str "Listening for http requests on :" port))
  (run-server handler {:port port}))


(defn stop-server [server]
  (when server
    (server)))


(defrecord Server [db]
  component/Lifecycle
  (start [this]
    (assoc this :server (start-server (make-handler db) 8090)))
  (stop [this]
    (stop-server (:server this))
    (dissoc this :server)))

(defn new-server []
  (component/using
   (map->Server {})
   [:db]))
