(ns halodb-native-demo.service.halodb
  (:require
   [com.stuartsierra.component :as component]
   [taoensso.timbre :as timbre]
   [clj-halodb.core :as halodb]))

(System/setProperty "org.caffinitas.ohc.allocator" "unsafe")

(def halodb-options
  (halodb/options
    {:max-file-size 131072
     :sync-write true}))

(defrecord DBComponent [options]
  component/Lifecycle
  (start [this]
    (timbre/infof "Starting DB...")
    (let [db (halodb/open ".halodb" halodb-options)]
      (assoc this :db db)))
  (stop [this]
    (timbre/infof "Stopping DB...")
    (let [db (:db this)]
      (when db
        (halodb/close db))
      (assoc this :db nil))))

(defn start-db [options]
  (map->DBComponent
    {:options options}))
