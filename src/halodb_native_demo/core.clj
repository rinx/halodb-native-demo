(ns halodb-native-demo.core
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [com.stuartsierra.component :as component]
   [taoensso.timbre :as timbre]
   [halodb-native-demo.usecase.system :as system])
  (:gen-class))

(set! *warn-on-reflection* true)

(def default-opts
  {:rest-api
   {:name "REST API"
    :port 8080
    :prestop-duration 10
    :routes [{:route "/"
              :body "root"}]}
   :liveness
   {:name "liveness"
    :port 3000
    :prestop-duration 10
    :routes [{:route "/liveness"
              :body "ok"}
             {:route "/shutdown"
              :shutdown true}]}
   :readiness
   {:name "readiness"
    :port 3001
    :prestop-duration 10
    :routes [{:route "/readiness"
              :body "ok"}]}})

(def timbre-config
  {:output-fn
   (fn [{:keys [level] :as data}]
     (timbre/color-str
       (case level
         :trace :cyan
         :debug :blue
         :info :green
         :warn :yellow
         :error :red
         :fatal :red
         :report :red)
       (timbre/default-output-fn data)))})

(def shutdown-hook (atom nil))

(defn run [config]
  (timbre/merge-config! timbre-config)
  (timbre/set-level! :debug)
  (let [opts (or config default-opts)
        system (system/system opts)]
    (component/start system)
    (reset! shutdown-hook #(component/stop system))))

(defn -main [& args]
  (-> (Runtime/getRuntime)
      (.addShutdownHook
       (proxy [Thread] []
         (run []
           (timbre/warn "ShutdownHook is called.")
           (timbre/info "System shutdown process started...")
           (let [shutdown-hook (deref shutdown-hook)]
             (when shutdown-hook
               (shutdown-hook)))
           (timbre/info "System shutdown process completed.")))))
  (let [filename (first args)
        config (when (and filename
                          (-> (io/file filename)
                              (.exists)))
                 (-> filename
                     (slurp)
                     (edn/read-string)))]
    (run config)))
