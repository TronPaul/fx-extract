(ns fx-extract.ofx
  (:require [ofx-clj.core :as ofx])
  (:import (net.sf.ofx4j.client.impl FinancialInstitutionServiceImpl BaseFinancialInstitutionData)
           (net.sf.ofx4j.domain.data.creditcard CreditCardAccountDetails)
           (java.net URL)))

(defn filter-empty [s]
  (map #(into {} (map (fn [[k v]]
                       (let [v (if (map? v)
                                 (filter-empty v)
                                 v)]
                         (if (not (or (and (or (coll? v) (string? v)) (empty? v)) (nil? v)))
                           [k v]))) %)) s))

(defn- create-cc-details [account-number]
  (let [cc-details (CreditCardAccountDetails.)]
    (.setAccountNumber cc-details account-number)
    cc-details))

(defn read-ofx-file [ofx-file]
  (filter-empty (:transactions (:transaction-list (:message (first (:messages (second (ofx/parse ofx-file)))))))))

(defn read-ofx-remote [fid account-number username password start-date end-date]
  (let [fi (.getFinancialInstitution (FinancialInstitutionServiceImpl.) fid)
        cc-details (create-cc-details account-number)
        cc-account (.loadCreditCardAccount fi cc-details username password)]
    (filter-empty (:transactions (:transaction-list (ofx/parse-data (.readStatement cc-account start-date end-date)))))))

(defn create-fid [fi-id name organization url]
  (let [fid (BaseFinancialInstitutionData.)]
    (.setFinancialInstitutionId fid fi-id)
    (.setName fid name)
    (.setOrganization fid organization)
    (.setOFXURL fid (URL. url))
    fid))