(ns confman.server
  (:require [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [com.stuartsierra.component :as component]
            [confman.db :refer [get-kvs]]))


(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello world"})


(defn kvs [req]
   {:body (get-kvs (::db req))})


(defroutes routes
  (GET "/" [] index)
  (GET "/v1/kvs" [] kvs)
  (route/not-found "<h1>Page not found</h1>"))


(defn wrap-db-component [f db]
  (fn [req]
    (f (assoc req ::db db))))


(defn make-handler [db]
  (-> routes
     (wrap-db-component db)))


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
