(ns halodb-native-demo.usecase.system
  (:require [com.stuartsierra.component :as component]
            [halodb-native-demo.service.server :as server]
            [halodb-native-demo.service.health :as health]
            [halodb-native-demo.service.halodb :as db]))

(defn system [{:keys [rest-api liveness readiness] :as conf}]
  (component/system-map
    :liveness (health/start-server liveness)
    :db (db/start-db {})
    :rest (component/using
            (server/start-server rest-api)
            {:liveness :liveness
             :db :db})
    :readiness (component/using
                 (health/start-server readiness)
                 {:rest :rest})))
