(defproject fx-extract "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ofx-clj "0.1"]
                 [net.sf.ofx4j/ofx4j "1.3"]]
  :main ^:skip-aot fx-extract.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
