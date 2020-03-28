(defproject halodb-native-demo "0.1.0-SNAPSHOT"
  :description "halodb GraalVM native demo"
  :url "https://github.com/rinx/halodb-native-demo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [org.clojure/core.async "0.7.559"]
                 [com.stuartsierra/component "0.4.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [clj-halodb "0.0.3"]]
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  :native-image
  {:name "halodb-native-demo"
   :opts ["-H:+ReportExceptionStackTraces"
          "-H:Log=registerResource:"
          "-H:ConfigurationFileDirectories=native-config"
          "--enable-url-protocols=http,https"
          "--enable-all-security-services"
          "--no-fallback"
          "--no-server"
          "--report-unsupported-elements-at-runtime"
          "--initialize-at-build-time"
          "--allow-incomplete-classpath"
          "--verbose"
          "--static"
          "-J-Xms1g"
          "-J-Xmx10g"]
   :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
  :profiles {:uberjar {:aot :all
                       :main halodb-native-demo.core}})
