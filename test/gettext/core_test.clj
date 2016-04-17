(ns gettext.core-test
  (:require [clojure.test :refer :all]
            [gettext.core :refer :all]))

(deftest gettext-test
  (testing "Literal replacement")
  (testing "Format replacement")
  (testing "Ignore format argument")
  (testing "Default to key when missing")
  (testing "Use empty context when replacement is a function"))

(deftest pgettext-test
  (testing "Change replacement based on context")
  (testing "Ignore context when replacement is not a function"))

; remove this in case already used above
(deftest source-test
  (testing "change *text-source* binding."))
