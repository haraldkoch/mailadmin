(ns mailadmin.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [mailadmin.core-test]))

(doo-tests 'mailadmin.core-test)

