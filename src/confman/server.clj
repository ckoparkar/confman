(ns confman.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))


(defn index [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello csk!"})


(defroutes app
  (GET "/" [] index)
  (route/not-found "<h1>Page not found</h1>"))
