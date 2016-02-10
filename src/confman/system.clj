(ns confman.system
  (:require [org.httpkit.server :refer [run-server]]
            [com.stuartsierra.component :as component]
            [confman.server :refer [app]]))


(defn start-server [app port]
  (println (str "Listening for http requests on :" port))
  (run-server app {:port port}))


(defn stop-server [server]
  (when server
    (server)))


(defrecord ConfMan []

  ;; Implement the Lifecycle protocol
  component/Lifecycle

  (start [this]
    (assoc this :server (start-server #'app 8090)))

  (stop [this]
    (stop-server (:server this))
    (dissoc this :server))

  )


(defn create-system []
  (ConfMan.))


(defn -main []
  (.start (create-system)))
