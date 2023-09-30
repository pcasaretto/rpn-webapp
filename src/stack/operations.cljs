(ns stack.operations)

(defn ensure-seq [x]
  (if (seq? x)
    x
    (list x)))

(defn n-ary-stack-operation
  [operation n]
  (fn [stack]
   (->> (take n stack)
        reverse
        (apply operation)
        ensure-seq
        (apply conj (drop n stack)))))

(defn singular-stack-operation
  [operation]
  (n-ary-stack-operation operation 1))

(defn pair-stack-operation
  [operation]
  (n-ary-stack-operation operation 2))

(def operators
  {:subtract (pair-stack-operation -)
   :sum (pair-stack-operation +)
   :multiply (pair-stack-operation *)
   :divide (pair-stack-operation /)
   :power (pair-stack-operation js/Math.pow)
   :inverse (singular-stack-operation (fn [x] (/ 1 x)))
   :negate (singular-stack-operation (fn [x] (- x)))
   :square-root (singular-stack-operation js/Math.sqrt)
   :factorial (singular-stack-operation (fn [x] (apply * (range 1 (inc x)))))
   :summation #(conj '() (reduce + %))
   :pop #(drop 1 %)
   :swap (pair-stack-operation (comp reverse list))})
