(defproject info.youhavethewrong/superdeduper "0.1.0-SNAPSHOT"
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha15"]
                 [digest "1.4.5"]]
  :aot :all
  :main superdeduper.core
  :uberjar-name "superdeduper.jar")
