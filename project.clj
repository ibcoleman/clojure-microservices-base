(defproject spa-om-clojure "1.0.0-SNAPSHOT"
  :description "Spa-om-clojure service"

  :dependencies [[ch.qos.logback/logback-classic "1.1.2"]
                 [cheshire "5.3.1"]
                 [clj-http "0.7.9"]
                 [clj-time "0.8.0"]
                 [compojure "1.2.0"]
                 [environ "1.0.0"]
                 [mixradio/graphite-filter "1.0.0"]
                 [mixradio/instrumented-ring-jetty-adapter "1.0.4"]
                 [mixradio/radix "1.0.5"]
                 [net.logstash.logback/logstash-logback-encoder "3.2"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ring-middleware-format "0.4.0"]]

  :exclusions [commons-logging
               log4j
               org.clojure/clojure]

  :profiles {:dev {:dependencies [[com.github.rest-driver/rest-client-driver "1.1.41"
                                   :exclusions [org.slf4j/slf4j-nop
                                                javax.servlet/servlet-api
                                                org.eclipse.jetty.orbit/javax.servlet]]
                                  [junit "4.11"]
                                  [midje "1.6.3"]
                                  [rest-cljer "0.1.11"]]
                   :plugins [[lein-kibit "0.0.8"]
                             [lein-midje "3.1.3"]
                             [lein-rpm "0.0.5"]]}}

  :plugins [[lein-environ "1.0.0"]
            [lein-release "1.0.5"]
            [lein-ring "0.8.12"]]

  :env {:environment-name "poke"
        :graphite-enabled false
        :graphite-host ""
        :graphite-port 2003
        :graphite-post-interval-seconds 60
        :logging-consolethreshold "info"
        :logging-filethreshold "info"
        :logging-level "info"
        :logging-path "/tmp"
        :logging-stashthreshold "off"
        :production false
        :requestlog-enabled false
        :requestlog-retainhours 24
        :restdriver-port 8081
        :service-name "spa-om-clojure"
        :service-port 8080
        :service-url "http://localhost:%s"
        :shutdown-timeout-millis 5000
        :start-timeout-seconds 120
        :threads 254
        :auto-reload true}

  :lein-release {:deploy-via :shell
                 :shell ["lein" "do" "clean," "uberjar," "pom," "rpm"]}

  :ring {:handler spa-om-clojure.web/app
         :main spa-om-clojure.setup
         :port ~(Integer/valueOf (get (System/getenv) "SERVICE_PORT" "8080"))
         :init spa-om-clojure.setup/setup
         :browser-uri "/healthcheck"
         :nrepl {:start? true}}

  :uberjar-name "spa-om-clojure.jar"

  :rpm {:name "spa-om-clojure"
        :summary "RPM for Spa-om-clojure service"
        :copyright "MixRadio 2014"
        :preinstall {:scriptFile "scripts/rpm/preinstall.sh"}
        :postinstall {:scriptFile "scripts/rpm/postinstall.sh"}
        :preremove {:scriptFile "scripts/rpm/preremove.sh"}
        :postremove {:scriptFile "scripts/rpm/postremove.sh"}
        :requires ["jdk >= 2000:1.7.0_55-fcs"]
        :mappings [{:directory "/usr/local/spa-om-clojure"
                    :filemode "444"
                    :username "spa-om-clojure"
                    :groupname "spa-om-clojure"
                    :sources {:source [{:location "target/spa-om-clojure.jar"}]}}
                   {:directory "/usr/local/spa-om-clojure/bin"
                    :filemode "744"
                    :username "spa-om-clojure"
                    :groupname "spa-om-clojure"
                    :sources {:source [{:location "scripts/bin"}]}}
                   {:directory "/etc/rc.d/init.d"
                    :filemode "755"
                    :sources {:source [{:location "scripts/service/spa-om-clojure"
                                        :destination "spa-om-clojure"}]}}]}

  :main spa-om-clojure.setup)
