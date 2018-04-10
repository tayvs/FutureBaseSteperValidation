package futureValidator

/**
  * Validation mark-object for controlling validation flow
  */
sealed trait ValidationStopper

object ValidationStopper {
  
  /**
    * Mark exception-object for validation stopping if step not init
    *
    * @param value -  last validated object state
    * @tparam T -  type ob validated object
    */
  case class ValidationStop[T](value: T) extends Exception() with ValidationStopper
  
  /**
    * Mark exception-object for error occur into step validation
    *
    * @param value -  last validated object state
    * @param ex    - exception that occur into validation
    * @tparam T -  type ob validated object
    */
  case class ValidationError[T](value: T, ex: Throwable) extends Exception(ex) with ValidationStopper
  object ValidationError {
    def apply[T](value: T, errorMsg: String): ValidationError[T] = ValidationError(value, new Exception(errorMsg))
  }
  
  /**
    * Mark exception-object for indicating full validation failing (occur if first validation step failed)
    */
  case object ValidationFailed extends Exception("Validation completely failed") with ValidationStopper
  
}
