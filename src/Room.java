(define (removeNegative L)
 (cond
  ((null? L) '())
  ((< (car L) 0) (removeNegative (cdr L)))
  (else (append L
       (removeNegative (cdr L))))
  ) )

display ((removeNegative '(1 -2 3 -3 -4 5 4)))