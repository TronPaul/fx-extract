(ns fx-extract.ofx
  (:require [ofx-clj.core :as ofx])
  (:import (net.sf.ofx4j.client.impl FinancialInstitutionServiceImpl BaseFinancialInstitutionData)
           (net.sf.ofx4j.domain.data.creditcard CreditCardAccountDetails)
           (java.net URL)
           (java.util Date)
           (java.time LocalDateTime ZoneId)))

(defn filter-empty [m]
  (into {} (map (fn [[k v]]
                  (let [v (if (map? v)
                            (filter-empty v)
                            v)]
                    (if (not (or (and (coll? v) (empty? v)) (nil? v)))
                      [k v]))) m)))

(defn- create-cc-details [account-number]
  (let [cc-details (CreditCardAccountDetails.)]
    (.setAccountNumber cc-details account-number)
    cc-details))

(defn read-ofx-file [ofx-file]
  (filter-empty (ofx/parse ofx-file)))

(defn read-ofx-remote [fid account-number username password]
  (let [fi (.getFinancialInstitution (FinancialInstitutionServiceImpl.) fid)
        cc-details (create-cc-details account-number)
        cc-account (.loadCreditCardAccount fi cc-details username password)]
    (filter-empty (ofx/parse-data (.readStatement cc-account (Date/from (.toInstant (.atZone (LocalDateTime/of 2016 6 1 0 0) (ZoneId/systemDefault)))) (Date.))))))

(defn create-fid [fi-id name organization url]
  (let [fid (BaseFinancialInstitutionData.)]
    (.setFinancialInstitutionId fid fi-id)
    (.setName fid name)
    (.setOrganization fid organization)
    (.setOFXURL fid (URL. url))
    fid))