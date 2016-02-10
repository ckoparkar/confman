(ns user
  (:require [reloaded.repl :refer [reset system stop]]
            [confman.system]))


(reloaded.repl/set-init! (confman.system/start-system))
